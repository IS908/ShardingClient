package com.sharding.client.jdbc.core.statement;

import com.sharding.client.jdbc.core.connection.ConnectionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.Map;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public class PreparedStatementProxy extends BaseDdfPreparedStatement implements PreparedStatement {
    private final static Logger logger = LoggerFactory.getLogger(PreparedStatementProxy.class);

    private final ConnectionProxy connection;

    public PreparedStatementProxy(String sql, ConnectionProxy connection) {
        super(sql);
        this.connection = connection;
    }

    public PreparedStatementProxy(Map<String, Object> attributeMap, String sql, ConnectionProxy connection) {
        super(sql, attributeMap);
        this.connection = connection;
    }
}
