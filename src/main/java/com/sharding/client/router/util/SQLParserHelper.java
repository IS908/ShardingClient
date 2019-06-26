package com.sharding.client.router.util;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.parser.DruidParser;
import com.sharding.client.router.parser.DruidParserFactory;

import java.sql.SQLException;

/**
 * @Description: 通过druid进行SQL语法分析
 * @Auther: kun
 * @Date: 2019-04-02 22:36
 */
public class SQLParserHelper {

    public static SQLStatement parser(String sql, String dbType) {
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        SQLStatement sqlAst = parser.parseStatement(false);
        return sqlAst;
    }

    public RouterUnit routeNormalSqlWithAST(SchemaConfig schema, String sql, String dbType) throws SQLException {
        SQLStatement statement = parser(sql, dbType);
        DruidParser druidParser = DruidParserFactory.create(statement);
        return RouterUtil.routeFromParser(druidParser, schema, statement);

    }
}
