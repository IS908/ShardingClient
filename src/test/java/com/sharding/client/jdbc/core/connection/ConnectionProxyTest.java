package com.sharding.client.jdbc.core.connection;

import com.sharding.client.jdbc.core.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-04 15:37
 */
public class ConnectionProxyTest extends BaseTest {

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        if (connection.isClosed()) {
            return;
        }
        connection.close();
    }

    @Test
    public void createStatement() throws Exception {
        Statement stm = connection.createStatement();
        String sql = "select * from test where seq_no = 0";
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next())
            System.out.println("test - seq_no ====" + rs.getObject(2));
        stm.close();
        tearDown();
    }

    @Test
    public void createStatement1() {
    }

    @Test
    public void createStatement2() {
    }

    @Test
    public void prepareStatement() {
    }

    @Test
    public void prepareStatement1() {
    }

    @Test
    public void prepareStatement2() {
    }

    @Test
    public void prepareStatement3() {
    }

    @Test
    public void prepareStatement4() {
    }

    @Test
    public void prepareStatement5() {
    }

    @Test
    public void commit() {
    }

    @Test
    public void rollback() {
    }
}