package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.router.RouterUnit;

import java.sql.SQLException;

/**
 * Parser SQLStatement
 *
 * @author lixch
 */
public interface DruidParser {

    void visitorParse(SchemaConfig schema, RouterUnit rn, SQLStatement stmt) throws SQLException;
}
