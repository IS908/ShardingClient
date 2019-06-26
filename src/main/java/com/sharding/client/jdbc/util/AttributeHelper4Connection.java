package com.sharding.client.jdbc.util;

import com.sharding.client.jdbc.api.ConnectionAttributeSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: Connection 相关属性传递
 * @Auther: kun
 * @Date: 2019-04-03 19:35
 */
public class AttributeHelper4Connection {
    private static final Logger logger = LoggerFactory.getLogger(AttributeHelper4Connection.class);

    private static final Map<String, ConnectionAttributeSetter> CONNECTION_ATTRIBUTE_SETTER_MAP;

    static {
        CONNECTION_ATTRIBUTE_SETTER_MAP = new HashMap<>();
        CONNECTION_ATTRIBUTE_SETTER_MAP.put(Constants.readonly, readonlySetter());
        CONNECTION_ATTRIBUTE_SETTER_MAP.put(Constants.holdability, holdabilitySetter());
        CONNECTION_ATTRIBUTE_SETTER_MAP.put(Constants.transaction_isolation, transactionIsolationSetter());
    }

    public static void setAttributes(Connection conn, Map<String, Object> attributes) throws SQLException {
        logger.debug("Connection attributes size: {}", attributes.size());
        Iterator<Map.Entry<String, Object>> iterator = attributes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            ConnectionAttributeSetter setter = CONNECTION_ATTRIBUTE_SETTER_MAP.get(entry.getKey());
            if (setter == null) {
                continue;
            }
            logger.debug("attribute: {} - {}", entry.getKey(), entry.getValue());
            setter.setAttribute(conn, entry.getValue());
        }
    }

    private static ConnectionAttributeSetter readonlySetter() {
        return (conn, val) -> {
            boolean readonly = (boolean) val;
            conn.setReadOnly(readonly);
        };
    }

    private static ConnectionAttributeSetter holdabilitySetter() {
        return (conn, val) -> {
            int holdability = (int) val;
            conn.setHoldability(holdability);
        };
    }

    private static ConnectionAttributeSetter transactionIsolationSetter() {
        return (conn, val) -> {
            int transaction_isolation = (int) val;
            conn.setTransactionIsolation(transaction_isolation);
        };
    }

}
