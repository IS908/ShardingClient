package com.sharding.client.jdbc.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Description: 延迟创建 PreparedStatement
 * @Auther: kun
 * @Date: 2019-04-03 16:28
 */
@FunctionalInterface
public interface LazyPrepareStatement {

    /**
     * 创建 PreparedStatement
     *
     * @param conn
     * @param sql
     * @return
     * @throws SQLException
     */
    PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException;

}
