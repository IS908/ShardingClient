package com.sharding.client.jdbc.core.statement;

import com.sharding.client.jdbc.unsupported.AbstractUnsupportedOperationStatement;
import com.sharding.client.jdbc.util.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public abstract class BaseDdfStatement extends AbstractUnsupportedOperationStatement implements Statement {

    private boolean closed = false;
    protected Map<String, Statement> statementHolder;
    protected Map<String, Statement> currentStatementHolder;

    public BaseDdfStatement(Map<String, Object> attributeMap) {
        super(attributeMap);
        this.statementHolder = new HashMap<>();
        this.currentStatementHolder = new HashMap<>();
    }

    public BaseDdfStatement() {
        this(new HashMap<>());
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        // todo
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        // todo
        return 0;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        // todo
        return 0;
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        Object max = getAttribute(Constants.max_field_size);
        if (max != null) {
            return (int) max;
        }
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        putAttribute(Constants.max_field_size, max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        Object max = getAttribute(Constants.max_rows);
        if (max != null) {
            return (int) max;
        }
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        putAttribute(Constants.max_rows, max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        putAttribute(Constants.escape_processing, enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        Object seconds = getAttribute(Constants.query_timeout);
        if (seconds != null) {
            return (int) seconds;
        }
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        putAttribute(Constants.query_timeout, seconds);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        putAttribute(Constants.fetch_size, rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        Object rows = getAttribute(Constants.fetch_size);
        if (rows != null) {
            return (int) rows;
        }
        return 0;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        putAttribute(Constants.poolable, poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        Object poolable = getAttribute(Constants.poolable);
        if (poolable != null) {
            return (boolean) poolable;
        }
        return false;
    }

    @Override
    public void close() throws SQLException {
        closed = true;
        clearAttributes();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public void cancel() throws SQLException {
        // todo:
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // todo
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        // todo
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // todo
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // todo
        return false;
    }
}
