package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.util.RouterUtil;

import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.List;

/**
 * @author lixch
 */
public class DruidUpdateParser extends DefaultDruidParser {
    public void visitorParse(SchemaConfig schema, RouterUnit ru, SQLStatement stmt)
            throws SQLException {
        MySqlUpdateStatement update = (MySqlUpdateStatement) stmt;
        SQLTableSource tableSource = update.getTableSource();
        if (tableSource instanceof SQLExprTableSource) {
            String tableName = ((SQLExprTableSource) tableSource).getName().toString();
            TableConfig tc = schema.getTables().get(tableName);
            if (tc == null) throw new SQLException("table " + tableName + " hasn't be found");
            String partitionColumn = tc.getPartitionColumn();
            String joinKey = tc.getJoinKey();
            confirmShardColumnNotUpdated(update, schema, tableName, partitionColumn, joinKey);
            //table has no sharding
            String noShardingNode = RouterUtil.isNoSharding(schema, tableName);
            if (noShardingNode != null) {
                ru.getDataNodes().add(noShardingNode);
                return;
            }

            super.visitorParse(stmt, tc);
            RouterUtil.calculateByColumn(ru, routePairs, tc);

        } else {
            RouterUtil.route2Ddf();
        }
    }

    private static boolean columnInExpr(SQLExpr sqlExpr, String colName) throws SQLNonTransientException {
        String column;
        if (sqlExpr instanceof SQLIdentifierExpr) {
            column = ((SQLIdentifierExpr) sqlExpr).getName();
        } else if (sqlExpr instanceof SQLPropertyExpr) {
            column = ((SQLPropertyExpr) sqlExpr).getName();
        } else {
            throw new SQLNonTransientException("Unhandled SQL AST node type encountered: " + sqlExpr.getClass());
        }

        return column.equals(colName.toUpperCase());
    }

    /*
     * isSubQueryClause
     * IN (select...), ANY, EXISTS, ALL , IN (1,2,3...)
     */
    private static boolean isSubQueryClause(SQLExpr sqlExpr) throws SQLNonTransientException {
        return (sqlExpr instanceof SQLInSubQueryExpr || sqlExpr instanceof SQLAnyExpr || sqlExpr instanceof SQLAllExpr ||
                sqlExpr instanceof SQLQueryExpr || sqlExpr instanceof SQLExistsExpr);
    }

    /**
     * check shardColCanBeUpdated
     * o the partition col is in OR/XOR filter,it will Failed.
     * eg :update mytab set ptn_col = val, col1 = val1 where col1 = val11 or ptn_col = val；
     * o if the set statement has the same value with the where condition,and we can router to some node
     * eg1:update mytab set ptn_col = val, col1 = val1 where ptn_col = val and (col1 = val11 or col2 = val2);
     * eg2 :update mytab set ptn_col = val, col1 = val1 where ptn_col = val and col1 = val11 and col2 = val2;
     * o update the partition column but partition column is not not in where filter. Failed
     * eg:update mytab set ptn_col = val, col1 = val1 where col1 = val11 and col2 = val22;
     * o the other operator, like between,not, Just Failed.
     *
     * @param whereClauseExpr
     * @param column
     * @param value
     * @return true Passed, false Failed
     * @hasOR the parent of whereClauseExpr hasOR/XOR
     */
    private boolean shardColCanBeUpdated(SQLExpr whereClauseExpr, String column, SQLExpr value, boolean hasOR)
            throws SQLNonTransientException {
        boolean canUpdate = false;
        boolean parentHasOR = false;

        if (whereClauseExpr == null)
            return false;

        if (whereClauseExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr nodeOpExpr = (SQLBinaryOpExpr) whereClauseExpr;
            /*
             * partition column exists in or/xor expr
             */
            if ((nodeOpExpr.getOperator() == SQLBinaryOperator.BooleanOr) ||
                    (nodeOpExpr.getOperator() == SQLBinaryOperator.BooleanXor)) {
                parentHasOR = true;
            }
            if (nodeOpExpr.getOperator() == SQLBinaryOperator.Equality) {
                boolean foundCol;
                SQLExpr leftExpr = nodeOpExpr.getLeft();
                SQLExpr rightExpr = nodeOpExpr.getRight();

                foundCol = columnInExpr(leftExpr, column);

                // col is partition column, 1.check it is in OR expr or not
                // 2.check the value is the same to update set expr
                if (foundCol) {
                    if (rightExpr.getClass() != value.getClass()) {
                        throw new SQLNonTransientException("SQL AST nodes type mismatch!");
                    }

                    canUpdate = rightExpr.toString().equals(value.toString()) && (!hasOR) && (!parentHasOR);
                }
            } else if (nodeOpExpr.getOperator().isLogical()) {
                if (nodeOpExpr.getLeft() != null) {
                    if (nodeOpExpr.getLeft() instanceof SQLBinaryOpExpr) {
                        canUpdate = shardColCanBeUpdated(nodeOpExpr.getLeft(), column, value, parentHasOR);
                    }
                    // else  !=,>,< between X and Y ,NOT ,just Failed
                }
                if ((!canUpdate) && nodeOpExpr.getRight() != null) {
                    if (nodeOpExpr.getRight() instanceof SQLBinaryOpExpr) {
                        canUpdate = shardColCanBeUpdated(nodeOpExpr.getRight(), column, value, parentHasOR);
                    }
                    // else  !=,>,< between X and Y ,NOT ,just Failed
                }
            } else if (isSubQueryClause(nodeOpExpr)) {
                // subQuery ,just Failed
                return false;
            }
            // else other expr,just Failed
        }
        // else  single condition but is not = ,just Failed
        return canUpdate;
    }

    private void confirmShardColumnNotUpdated(SQLUpdateStatement update, SchemaConfig schema, String tableName, String partitionColumn, String joinKey) throws SQLNonTransientException {
        List<SQLUpdateSetItem> updateSetItem = update.getItems();
        if (updateSetItem != null && updateSetItem.size() > 0) {
            boolean hasParent = (schema.getTables().get(tableName).getParentTC() != null);
            for (SQLUpdateSetItem item : updateSetItem) {
                String column = item.getColumn().toString();
                if (partitionColumn != null && partitionColumn.equals(column)) {
                    boolean canUpdate;
                    canUpdate = ((update.getWhere() != null) && shardColCanBeUpdated(update.getWhere(),
                            partitionColumn, item.getValue(), false));

                    if (!canUpdate) {
                        String msg = "Sharding column can't be updated " + tableName + "->" + partitionColumn;
                        throw new SQLNonTransientException(msg);
                    }
                }
                if (hasParent) {
                    if (column.equals(joinKey)) {
                        String msg = "Parent relevant column can't be updated " + tableName + "->" + joinKey;
                        throw new SQLNonTransientException(msg);
                    }
                }
            }
        }
    }

}
