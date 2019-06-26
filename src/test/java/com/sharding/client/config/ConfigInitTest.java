package com.sharding.client.config;

import com.sharding.client.config.model.DBHostConfig;
import com.sharding.client.config.model.DataHostConfig;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.util.SQLParserHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;

public class ConfigInitTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigInitTest.class);

    /**
     * 测试加载schemas 以及表配置
     */
    @Test
    public void testInitConfig() throws SQLException {
        DdfClientConfig ddfClientConfig = DdfClientConfig.getInstance();
        Map<String, SchemaConfig> schemas = ddfClientConfig.getSchemas();
        for (Map.Entry<String, SchemaConfig> entry : schemas.entrySet()) {
            SchemaConfig schemaConfig = entry.getValue();
            logger.debug("schema name : " + schemaConfig.getName());
            Map<String, TableConfig> tables = schemaConfig.getTables();
            TableConfig tableConfig = tables.get("mb_acct_test");
            if (tableConfig != null) {
                logger.debug(tableConfig.getName() + " --- " + tableConfig.getPrimaryKey());
            }

            SQLParserHelper parserHelper = new SQLParserHelper();
            String sql = "select * from mb_acct_test where name = 123";
            sql = "delete mb_acct_test where id = 123";
            sql = "update mb_acct_test set sdfd = 'sdfa' where id = 12543";
            sql = "insert into mb_acct_test (fjklds, fas) value(11,11)";
            System.out.println(parserHelper.routeNormalSqlWithAST(schemaConfig, sql, "mysql"));
        }
    }

    /**
     * 测试加载dataHost相关配置
     */
    @Test
    public void testInitDataHost() {
        DdfClientConfig clientConfig = DdfClientConfig.getInstance();
        Map<String, DataHostConfig> dataHosts = clientConfig.getDataHosts();
        for (Map.Entry<String, DataHostConfig> entry : dataHosts.entrySet()) {
            DataHostConfig dataHostConfig = entry.getValue();
            logger.debug("dataHostName : " + entry.getKey() + "heartbeat sql :" + dataHostConfig.getHearbeatSQL());
            DBHostConfig[] writeHosts = dataHostConfig.getWriteHosts();
            for (DBHostConfig config : writeHosts) {
                logger.debug("host ： " + config.getHostName() + " url :" + config.getUrl() + " user: " + config.getUser());
            }
        }
    }
}
