package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.util.RouterUtil;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lixch
 */
public class DruidDeleteParser extends DefaultDruidParser {
    @Override
    public void visitorParse(SchemaConfig schema, RouterUnit ru, SQLStatement stmt)
            throws SQLException {
        Set<String> dataNode = new HashSet<>();
        MySqlDeleteStatement delete = (MySqlDeleteStatement) stmt;
        SQLTableSource tableSource = delete.getTableSource();
        SQLTableSource fromSource = delete.getFrom();
        if (fromSource != null) {
            tableSource = fromSource;
        }
        if (tableSource instanceof SQLExprTableSource) {
            SQLExprTableSource deleteTableSource = (SQLExprTableSource) tableSource;
            String tableName = deleteTableSource.getName().toString();

            //table has no sharding
            String noShardingNode = RouterUtil.isNoSharding(schema, tableName);
            if (noShardingNode != null) {
                dataNode.add(noShardingNode);
                ru.setDataNodes(dataNode);
            }
            TableConfig tc = schema.getTables().get(tableName);
            if (tc == null) throw new SQLException("table " + tableName + " hasn't be found");
            super.visitorParse(stmt, tc);
            RouterUtil.calculateByColumn(ru, routePairs, tc);

            if (tc.isGlobalTable()) {
                RouterUtil.route2Ddf();
            }
        } else {
            RouterUtil.route2Ddf();
        }
    }
}
