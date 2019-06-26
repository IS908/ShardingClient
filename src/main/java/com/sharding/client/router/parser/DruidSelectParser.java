package com.sharding.client.router.parser;


import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.util.RouterUtil;

import java.sql.SQLException;
import java.sql.SQLNonTransientException;

public class DruidSelectParser extends DefaultDruidParser {
    @Override
    public void visitorParse(SchemaConfig schema, RouterUnit ru, SQLStatement stmt) throws SQLException {
        SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;
        SQLSelectQuery sqlSelectQuery = selectStmt.getSelect().getQuery();
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock mysqlSelectQuery = (SQLSelectQueryBlock) sqlSelectQuery;
            if (mysqlSelectQuery.getInto() != null) {
                throw new SQLNonTransientException("select ... into is not supported!");
            }
            SQLTableSource mysqlFrom = mysqlSelectQuery.getFrom();

            if (mysqlFrom instanceof SQLExprTableSource) {
                SQLExprTableSource fromSource = (SQLExprTableSource) mysqlFrom;
                TableConfig tc = schema.getTables().get(fromSource.getName());
                if (tc == null) throw new SQLException("table " + fromSource.getName() + " hasn't be found");
                super.visitorParse(stmt, tc);
                if (routePairs.size() == 0) {
                    RouterUtil.route2Ddf();
                }
                RouterUtil.calculateByColumn(ru, routePairs, tc);
            } else {
                RouterUtil.route2Ddf();
            }
        } else {
            RouterUtil.route2Ddf();
        }
    }
}
