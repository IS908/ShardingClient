package com.sharding.client.jdbc.core.datasource;

import com.sharding.client.jdbc.core.DdfDataSourceHelper;
import com.sharding.client.jdbc.unsupported.AbstractUnsupportedOperationDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public abstract class BaseDdfDatasource extends AbstractUnsupportedOperationDataSource implements DataSource {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseDdfDatasource.class);

    private DataSource target;
    private String schema;

    public BaseDdfDatasource(DataSource target, String schema) {
        this.target = target;
        this.schema = schema;
        DdfDataSourceHelper.getInstance().init(target);
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return target.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return target.getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        logger.info("unwrap: {}", iface);
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        logger.info("isWrapperFor: {}", iface);
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return target.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        target.setLogWriter(out);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return target.getParentLogger();
    }

}
