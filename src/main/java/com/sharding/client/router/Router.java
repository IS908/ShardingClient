package com.sharding.client.router;

import com.sharding.client.config.DdfClientConfig;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.router.util.SQLParserHelper;

import java.sql.SQLException;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public class Router {

    public static RouterUnit route(String sql) {
        return new RouterUnit();
    }

    public static RouterUnit route(String schmal, String sql) throws SQLException {
        SchemaConfig schema = DdfClientConfig.getInstance().getSchemas().get(schmal);
        SQLParserHelper parserHelper = new SQLParserHelper();
        return parserHelper.routeNormalSqlWithAST(schema, sql, "mysql");
    }

}
