package com.sharding.client.jdbc.api;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description:
 * @Auther: kun
 * @Date: 2019-04-02
 */
@FunctionalInterface
public interface StatementAttributeSetter {

    void setAttribute(Statement stmt, Object value) throws SQLException;

}
