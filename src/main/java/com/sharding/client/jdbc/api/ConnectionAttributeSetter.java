package com.sharding.client.jdbc.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Auther: kun
 * @Date: 2019-04-03 19:38
 */
@FunctionalInterface
public interface ConnectionAttributeSetter {

    void setAttribute(Connection conn, Object value) throws SQLException;

}
