package com.sharding.client.jdbc.unsupported;

import com.sharding.client.jdbc.proxy.BaseDDFProxy;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Map;

/**
 * Unsupported {@code Statement} methods.
 *
 * @author gaohongtao
 */
public abstract class AbstractUnsupportedOperationStatement extends BaseDDFProxy implements Statement {

    public AbstractUnsupportedOperationStatement(Map<String, Object> attributeMap) {
        super(attributeMap);
    }

    public AbstractUnsupportedOperationStatement() {
    }

    @Override
    public final int getFetchDirection() throws SQLException {
        throw new SQLFeatureNotSupportedException("getFetchDirection");
    }

    @Override
    public final void setFetchDirection(final int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException("setFetchDirection");
    }

    @Override
    public final void addBatch(final String sql) throws SQLException {
        throw new SQLFeatureNotSupportedException("addBatch sql");
    }

    @Override
    public void clearBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("clearBatch");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw new SQLFeatureNotSupportedException("executeBatch");
    }

    @Override
    public final void closeOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("closeOnCompletion");
    }

    @Override
    public final boolean isCloseOnCompletion() throws SQLException {
        throw new SQLFeatureNotSupportedException("isCloseOnCompletion");
    }

    @Override
    public final void setCursorName(final String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("setCursorName");
    }
}
