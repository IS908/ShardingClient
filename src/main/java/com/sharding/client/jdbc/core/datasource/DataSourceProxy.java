package com.sharding.client.jdbc.core.datasource;

import com.sharding.client.jdbc.core.connection.ConnectionProxy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public class DataSourceProxy extends BaseDdfDatasource implements DataSource {
    public DataSourceProxy(DataSource target, String schema) {
        super(target, schema);
    }


    @Override
    public Connection getConnection() throws SQLException {
        Connection target = super.getConnection();
        ConnectionProxy connection = new ConnectionProxy(target, this);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection target = super.getConnection(username, password);
        ConnectionProxy connection = new ConnectionProxy(target, this);
        return connection;
    }
}
