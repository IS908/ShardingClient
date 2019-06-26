package com.sharding.client.router.util;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.sharding.client.config.model.SchemaConfig;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.RouterUnit;
import com.sharding.client.router.function.AbstractPartitionAlgorithm;
import com.sharding.client.router.parser.DruidParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 路由结果实体类
 * @Auther: kun
 * @Date: 2019-04-03 10:40
 */
public class RouterUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterUtil.class);

    public static RouterUnit routeFromParser(DruidParser druidParser, SchemaConfig schema, SQLStatement statement) throws SQLException {
        RouterUnit ru = new RouterUnit();
        druidParser.visitorParse(schema, ru, statement);
        if (ru.isFinishedRoute()) {
            return ru;
        } else {
            route2Ddf();
            return null;
        }
    }

    public static void calculateByColumn(RouterUnit ru, List<ColumnRoutePair> partitionValue, TableConfig tableConfig) throws SQLException {
        if (partitionValue.size() == 0) route2Ddf();
        Set<String> routeNodeSet = new LinkedHashSet<>();
        for (ColumnRoutePair pair : partitionValue) {
            AbstractPartitionAlgorithm algorithm = tableConfig.getRule().getRuleAlgorithm();
            if (pair.colValue != null) {
                Integer nodeIndex;
                try {
                    nodeIndex = algorithm.calculate(pair.colValue);
                } catch (Exception e) {
                    throw e;
                }
                if (nodeIndex == null) {
                    String msg = "can't find any valid data node :" + tableConfig.getName() +
                            " -> " + tableConfig.getPartitionColumn() + " -> " + pair.colValue;
                    LOGGER.info(msg);
                    throw new SQLNonTransientException(msg);
                }

                ArrayList<String> dataNodes = tableConfig.getDataNodes();
                String node;
                if (nodeIndex >= 0 && nodeIndex < dataNodes.size()) {
                    node = dataNodes.get(nodeIndex);
                } else {
                    String msg = "Can't find a valid data node for specified node index :" +
                            tableConfig.getName() + " -> " + tableConfig.getPartitionColumn() +
                            " -> " + pair.colValue + " -> " + "Index : " + nodeIndex;
                    LOGGER.info(msg);
                    throw new SQLNonTransientException(msg);
                }
                if (node != null) {
                    routeNodeSet.add(node);
                }
            }
        }
        if (routeNodeSet.size() > 0) {
            ru.setFinishedRoute(true);
            ru.setDataNodes(routeNodeSet);
        } else {
            route2Ddf();
        }
    }

    public static void buildRouterUnit() {

    }

    public static String isNoSharding(SchemaConfig schema, String tableName) {
        return null;
    }

    public static void route2Ddf() throws SQLException {
        throw new SQLException("can't run on ddf-client");
    }
}
