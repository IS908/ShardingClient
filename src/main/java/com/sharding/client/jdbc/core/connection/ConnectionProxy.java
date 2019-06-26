package com.sharding.client.jdbc.core.connection;

import com.sharding.client.jdbc.api.LazyPrepareStatement;
import com.sharding.client.jdbc.core.DdfDataSourceHelper;
import com.sharding.client.jdbc.core.datasource.BaseDdfDatasource;
import com.sharding.client.jdbc.core.statement.PreparedStatementProxy;
import com.sharding.client.jdbc.core.statement.StatementProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public class ConnectionProxy extends BaseDdfConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionProxy.class);

    private final Map<String, Connection> connection4close;
    private final Map<String, Connection> connection4Commit;

    public ConnectionProxy(Connection target, BaseDdfDatasource datasource) {
        super(target, datasource);
        this.connection4close = new HashMap<>();
        this.connection4Commit = new HashMap<>();
    }

    public Connection getConnection(String connectionKey) throws SQLException {
        Connection conn = connection4close.get(connectionKey);
        if (conn == null) {
            conn = DdfDataSourceHelper.getInstance().getConnection(connectionKey);
            if (conn.getAutoCommit() != getAutoCommit()) {
                conn.setAutoCommit(getAutoCommit());
            }
        }
        if (!getAutoCommit()) {
            connection4Commit.putIfAbsent(connectionKey, conn);
        }
        return conn;
    }

    @Override
    public void close() throws SQLException {
        for (Connection connection : connection4close.values()) {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
        super.close();
    }

    @Override
    public Statement createStatement() throws SQLException {
        beforeCreateStatement();
        StatementProxy stmtProxy = new StatementProxy(this);
        afterCreateStatement(stmtProxy);
        return stmtProxy;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        beforeCreateStatement();
        StatementProxy stmtProxy = new StatementProxy(this);
        afterCreateStatement(stmtProxy);
        return stmtProxy;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        beforeCreateStatement();
        StatementProxy stmtProxy = new StatementProxy(this);
        afterCreateStatement(stmtProxy);
        return stmtProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs = (conn, newSql) -> conn.prepareStatement(sql);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs = (conn, newSql) -> conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs =
                (conn, newSql) -> conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs = (conn, newSql) -> conn.prepareStatement(newSql, autoGeneratedKeys);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs = (conn, newSql) -> conn.prepareStatement(newSql, columnIndexes);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        beforePrepareStatement();
        LazyPrepareStatement lazyPs = (conn, newSql) -> conn.prepareStatement(newSql, columnNames);
        PreparedStatementProxy psProxy = new PreparedStatementProxy(sql, this);
        afterPrepareStatement(psProxy);
        return psProxy;
    }

    private void beforeCreateStatement() {

    }

    private void afterCreateStatement(StatementProxy stmtProxy) {

    }

    private void beforePrepareStatement() {

    }

    private void afterPrepareStatement(PreparedStatementProxy psProxy) {

    }

    @Override
    public void commit() throws SQLException {
        beforeCommit();
        for (Connection connection : connection4Commit.values()) {
            connection.commit();
        }
        afterCommit();
    }

    @Override
    public void rollback() throws SQLException {
        beforeRollback();
        for (Connection connection : connection4Commit.values()) {
            connection.rollback();
        }
        afterRollback();
    }

    private void beforeCommit() throws SQLException {
    }

    private void afterCommit() {
        connection4Commit.clear();
    }

    private void beforeRollback() throws SQLException {
    }

    private void afterRollback() {
        connection4Commit.clear();
    }

}
