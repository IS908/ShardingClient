package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.function.AbstractPartitionAlgorithm;
import com.sharding.client.router.util.RouterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DruidInsertParser extends DefaultDruidParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidInsertParser.class);

    @Override
    public void visitorParse(SchemaConfig schema, RouterUnit ru, SQLStatement stmt)
            throws SQLException {

        SQLInsertStatement insert = (SQLInsertStatement) stmt;
        SQLExprTableSource tableSource = insert.getTableSource();

        String tableName = tableSource.getName().toString();
        TableConfig tc = schema.getTables().get(tableName);
        if (tc == null) throw new SQLException("table " + tableName + " hasn't be found");

        //table has no sharding
        if (parserNoSharding()) {
            return;
        }

        if (insert.getQuery() != null) {
            // insert into .... select ....
            String msg = "`INSERT ... SELECT Syntax` is not supported!";
            LOGGER.info(msg);
            throw new SQLNonTransientException(msg);
        }

        //自增列处理，暂不处理
        if (tc.isAutoIncrement()) {
            RouterUtil.route2Ddf();
        }

        String partitionColumn = tc.getPartitionColumn();
        if (partitionColumn != null) {
            if (isMultiInsert(insert)) {
                RouterUtil.route2Ddf();
            } else {
                parserSingleInsert(tc, ru, partitionColumn, insert);
            }
        } else {
            RouterUtil.route2Ddf();
        }
    }

    private boolean parserNoSharding() {
        return false;
    }

    /**
     * insert into ...values (),()... or insert into ...select.....
     *
     * @param insertStmt insertStmt
     * @return is Multi-Insert or not
     */
    private boolean isMultiInsert(SQLInsertStatement insertStmt) {
        return (insertStmt.getValuesList() != null && insertStmt.getValuesList().size() > 1);
    }

    /**
     * @param ru              RouteResultset
     * @param partitionColumn partitionColumn
     * @param insertStmt      insertStmt
     * @throws SQLNonTransientException if not find an valid data node
     */
    private void parserSingleInsert(TableConfig tableConfig, RouterUnit ru, String partitionColumn,
                                    SQLInsertStatement insertStmt) throws SQLException {
        Set<String> nodes = new HashSet<>();
        int shardingColIndex = tryGetShardingColIndex(insertStmt, partitionColumn);
        SQLExpr valueExpr = insertStmt.getValues().getValues().get(shardingColIndex);
        String shardingValue = shardingValueToSting(valueExpr);
        AbstractPartitionAlgorithm algorithm = tableConfig.getRule().getRuleAlgorithm();
        Integer nodeIndex = algorithm.calculate(shardingValue);
        if (nodeIndex == null || nodeIndex >= tableConfig.getDataNodes().size()) {
            String msg = "can't find any valid data node :" + tableConfig.getName() + " -> " + partitionColumn + " -> " + shardingValue;
            LOGGER.info(msg);
            throw new SQLNonTransientException(msg);
        }
        ArrayList<String> dataNodes = tableConfig.getDataNodes();
        String node = dataNodes.get(nodeIndex);
        nodes.add(node);

        // insert into .... on duplicateKey
        //such as :INSERT INTO TABLEName (a,b,c) VALUES (1,2,3) ON DUPLICATE KEY UPDATE b=VALUES(b);
        //INSERT INTO TABLEName (a,b,c) VALUES (1,2,3) ON DUPLICATE KEY UPDATE c=c+1;
        if (insertStmt instanceof MySqlInsertStatement && ((MySqlInsertStatement) insertStmt).getDuplicateKeyUpdate() != null) {
            List<SQLExpr> updateList = ((MySqlInsertStatement) insertStmt).getDuplicateKeyUpdate();
            for (SQLExpr expr : updateList) {
                SQLBinaryOpExpr opExpr = (SQLBinaryOpExpr) expr;
                String column = opExpr.getLeft().toString().toUpperCase();
                if (column.equals(partitionColumn)) {
                    String msg = "Sharding column can't be updated: " + tableConfig.getName() + " -> " + partitionColumn;
                    LOGGER.info(msg);
                    throw new SQLNonTransientException(msg);
                }
            }
        }
        ru.setFinishedRoute(true);
        ru.setDataNodes(nodes);
    }

    /**
     * find the index of the partition column
     *
     * @param insertStmt      MySqlInsertStatement
     * @param partitionColumn partitionColumn
     * @return the index of the partition column
     * @throws SQLNonTransientException if not find
     */
    private int tryGetShardingColIndex(SQLInsertStatement insertStmt, String partitionColumn)
            throws SQLException {

        int shardingColIndex = getShardingColIndex(insertStmt.getColumns(), partitionColumn);
        if (shardingColIndex != -1) return shardingColIndex;
        throw new SQLNonTransientException("bad insert sql, sharding column/joinKey:" + partitionColumn + " not provided," + insertStmt);
    }

    protected int getShardingColIndex(List<SQLExpr> columnExprList, String partitionColumn) throws SQLException {
        int shardingColIndex = -1;
        if (columnExprList == null || columnExprList.size() == 0) {
            RouterUtil.route2Ddf();
            return shardingColIndex;
        }
        for (int i = 0; i < columnExprList.size(); i++) {
            if (partitionColumn.equalsIgnoreCase(columnExprList.get(i).toString())) {
                return i;
            }
        }
        return shardingColIndex;
    }

    protected static String shardingValueToSting(SQLExpr valueExpr) throws SQLNonTransientException {
        String shardingValue = null;
        if (valueExpr instanceof SQLIntegerExpr) {
            SQLIntegerExpr intExpr = (SQLIntegerExpr) valueExpr;
            shardingValue = intExpr.getNumber() + "";
        } else if (valueExpr instanceof SQLCharExpr) {
            SQLCharExpr charExpr = (SQLCharExpr) valueExpr;
            shardingValue = charExpr.getText();
        }
        if (shardingValue == null) {
            throw new SQLNonTransientException("Not Supported of Sharding Value EXPR :" + valueExpr.toString());
        }
        return shardingValue;
    }
}
