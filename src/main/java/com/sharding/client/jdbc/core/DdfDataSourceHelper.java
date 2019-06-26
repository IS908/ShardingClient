package com.sharding.client.jdbc.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sharding.client.config.DdfClientConfig;
import com.sharding.client.config.model.DBHostConfig;
import com.sharding.client.config.model.DataHostConfig;
import com.sharding.client.config.model.DataNodeConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: 直连分片对应的连接池
 * @Auther: kun
 * @Date: 2019-04-04 10:08
 */
public class DdfDataSourceHelper {
    private static final Logger logger = LoggerFactory.getLogger(DdfDataSourceHelper.class);

    private Map<String, DataSource> dataSourceMap;
    private boolean inited;

    private DdfDataSourceHelper() {
        dataSourceMap = new HashMap<>();
    }

    private static class SingletonHolder {
        private static DdfDataSourceHelper INSTANCE = new DdfDataSourceHelper();
    }

    public static DdfDataSourceHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Connection getConnection(String dsId) throws SQLException {
        if (!inited) {
            throw new RuntimeException("Not init yet! Please call method: init(dataSource) first");
        }
        DataSource ds = dataSourceMap.get(dsId);
        if (ds == null) {
            throw new RuntimeException(String.format("not DataSource by ID: %s", dsId));
        }

        return ds.getConnection();
    }

    public void init(DataSource oriDataSource) {
        if (inited) {
            logger.warn("DdfDataSourceHelper has already inited!");
            return;
        }
        initDataSource(oriDataSource);
        inited = true;
    }

    /**
     * 初始化直连分片数据库连接池
     */
    private void initDataSource(DataSource oriDataSource) {
        if (oriDataSource instanceof DruidDataSource) {
            initDruidDataSource();
        } else if (oriDataSource instanceof HikariDataSource) {
            initHikariDataSource();
        } else {
            throw new RuntimeException(String.format("Not Support DataSource: %s", oriDataSource.getClass().getName()));
        }
    }

    /**
     * Druid 连接池初始化
     */
    private void initDruidDataSource() {
        Iterator<Map.Entry<String, DataNodeConfig>> iterator = DdfClientConfig.getInstance()
                                                                              .getDataNodes()
                                                                              .entrySet()
                                                                              .iterator();
        Properties properties = new Properties();
        properties.setProperty("initialSize", "0");
        properties.setProperty("maxActive", "50");
        properties.setProperty("minIdle", "0");
        properties.setProperty("poolPreparedStatement", "true");
        properties.setProperty("maxOpenPreparedStatement", "50");
        properties.setProperty("testOnBorrow", "false");
        properties.setProperty("testOnReturn", "false");
        properties.setProperty("testWhileIdle", "true");

        while (iterator.hasNext()) {
            Map.Entry<String, DataNodeConfig> entry = iterator.next();
            DataNodeConfig config = entry.getValue();
            DataHostConfig dataHostConfig = DdfClientConfig.getInstance()
                                                           .getDataHosts()
                                                           .get(config.getDataHost());
            DBHostConfig dbHostConfig = dataHostConfig.getWriteHosts()[0];

            properties.setProperty("url", dbHostConfig.getUrl());
            properties.setProperty("username", dbHostConfig.getUser());
            properties.setProperty("password", dbHostConfig.getPassword());
            DruidDataSource druidDataSource = new DruidDataSource();
            try {
                DruidDataSourceFactory.config(druidDataSource, properties);
                druidDataSource.init();
            } catch (SQLException e) {
                throw new RuntimeException(String.format("create new datasource error for: %s", properties));
            }

            this.dataSourceMap.put(config.getName(), druidDataSource);
        }
    }

    /**
     * Hikari 连接池初始化
     */
    private void initHikariDataSource() {

        Iterator<Map.Entry<String, DataNodeConfig>> iterator = DdfClientConfig.getInstance()
                                                                              .getDataNodes()
                                                                              .entrySet()
                                                                              .iterator();
        while (iterator.hasNext()) {
            DataNodeConfig nodeConfig = iterator.next().getValue();
            DataHostConfig dataHostConfig = DdfClientConfig.getInstance()
                                                           .getDataHosts()
                                                           .get(nodeConfig.getDataHost());
            DBHostConfig dbHostConfig = dataHostConfig.getWriteHosts()[0];

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbHostConfig.getUrl());
            config.setUsername(dbHostConfig.getUser());
            config.setPassword(dbHostConfig.getPassword());
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "100");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "512");
            HikariDataSource hikariDataSource = new HikariDataSource(config);

            this.dataSourceMap.put(nodeConfig.getName(), hikariDataSource);
        }

    }
}
