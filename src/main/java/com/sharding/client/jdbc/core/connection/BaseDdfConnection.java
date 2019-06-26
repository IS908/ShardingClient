package com.sharding.client.jdbc.core.connection;

import com.sharding.client.jdbc.core.datasource.BaseDdfDatasource;
import com.sharding.client.jdbc.unsupported.AbstractUnsupportedOperationConnection;
import com.sharding.client.jdbc.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public abstract class BaseDdfConnection extends AbstractUnsupportedOperationConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(BaseDdfConnection.class);

    private Connection target;
    private BaseDdfDatasource datasource;

    private boolean autoCommit = true;
    private boolean closed = false;

    public BaseDdfConnection(Connection target, BaseDdfDatasource datasource) {
        this.target = target;
        this.datasource = datasource;
    }

    public Connection getTarget() {
        return this.target;
    }

    protected void checkClose() {
        if (closed) {
            throw new RuntimeException("connection has already closed!");
        }
    }

    public BaseDdfDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

        this.autoCommit = autoCommit;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    @Override
    public void close() throws SQLException {
        this.closed = true;
        target.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        putAttribute(Constants.readonly, readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        Object readonly = getAttribute(Constants.readonly);
        if (readonly != null) {
            return (boolean) readonly;
        }
        return false;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        putAttribute(Constants.transaction_isolation, level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        Object level = getAttribute(Constants.transaction_isolation);
        if (level != null) {
            return (int) level;
        }
        return Connection.TRANSACTION_READ_COMMITTED;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        putAttribute(Constants.holdability, holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        // ResultSet.CLOSE_CURSORS_AT_COMMIT;
        // ResultSet.HOLD_CURSORS_OVER_COMMIT;
        Object val = getAttribute(Constants.holdability);
        if (val != null) {
            return (int) val;
        }
        return 0;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return target.getMetaData();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return target.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        target.clearWarnings();
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
}
