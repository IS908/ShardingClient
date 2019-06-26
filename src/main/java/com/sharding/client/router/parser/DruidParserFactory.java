package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.sharding.client.router.util.RouterUtil;

import java.sql.SQLException;

/**
 * DruidParserFactory
 *
 * @author lixch
 */
public final class DruidParserFactory {
    private DruidParserFactory() {
    }

    public static DruidParser create(SQLStatement statement) throws SQLException {
        DruidParser parser = null;
        if (statement instanceof SQLSelectStatement) {
            parser = new DruidSelectParser();
        } else if (statement instanceof SQLInsertStatement) {
            parser = new DruidInsertParser();
        } else if (statement instanceof SQLDeleteStatement) {
            parser = new DruidDeleteParser();
        } else if (statement instanceof SQLUpdateStatement) {
            parser = new DruidUpdateParser();
        } else {
            RouterUtil.route2Ddf();
        }

        return parser;
    }
}
