package com.sharding.client.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.sharding.client.jdbc.core.datasource.DataSourceProxy;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-04 15:23
 */
public abstract class BaseTest {

    private static String url = "jdbc:mysql://127.0.0.1:8071/TestDb";
    private static String user = "root";
    private static String pwd = "123456";

    protected static DataSource dataSource;

    @BeforeClass
    public static void beforeAll() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(pwd);
        try {
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource = new DataSourceProxy(druidDataSource, "TestDb");
    }

    @AfterClass
    public static void afterAll() {

    }
}
