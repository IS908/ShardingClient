package com.sharding.client.jdbc.util;

import com.sharding.client.jdbc.api.StatementAttributeSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: Statement/PreparedStatement 属性传递
 * @Auther: kun
 * @Date: 2019-04-02
 */
public class AttributeHelper4Statement {

    private static final Logger logger = LoggerFactory.getLogger(AttributeHelper4Statement.class);

    private static final Map<String, StatementAttributeSetter> ATTRIBUTE_SETTER_MAP;

    static {
        ATTRIBUTE_SETTER_MAP = new HashMap<>();
        ATTRIBUTE_SETTER_MAP.put(Constants.close_on_completion, closeOnCompletionSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.cursor_name, cursorNameSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.escape_processing, escapeProcessingSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.fetch_direction, fetchDirectionSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.fetch_size, fetchSizeSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.max_field_size, maxFieldSizeSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.max_rows, maxRowsSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.poolable, poolableSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.query_timeout, queryTimeoutSetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.result_set_concurrency, resultSetConcurrencySetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.result_set_holdability, resultSetHoldabilitySetter());
        ATTRIBUTE_SETTER_MAP.put(Constants.result_set_type, resultSetTypeSetter());
    }

    public static void setAttributes(Statement stmt, Map<String, Object> attributes) throws SQLException {
        logger.debug("Statement attributes size: {}", attributes.size());
        Iterator<Map.Entry<String, Object>> it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            StatementAttributeSetter setter = ATTRIBUTE_SETTER_MAP.get(entry.getKey());
            if (setter == null) {
                continue;
            }
            logger.debug("attribute: {} - {}", entry.getKey(), entry.getValue());
            setter.setAttribute(stmt, entry.getValue());
        }
    }

    private static StatementAttributeSetter closeOnCompletionSetter() {
        return (stmt, val) -> {
            boolean isCloseOnCompletion = (boolean) val;
            if (isCloseOnCompletion) {
                stmt.closeOnCompletion();
            }
        };
    }

    private static StatementAttributeSetter maxFieldSizeSetter() {
        return (stmt, val) -> {
            int max = (int) val;
            stmt.setMaxFieldSize(max);
        };
    }

    private static StatementAttributeSetter maxRowsSetter() {
        return (stmt, val) -> {
            int max = (int) val;
            stmt.setMaxRows(max);
        };
    }

    private static StatementAttributeSetter escapeProcessingSetter() {
        return (stmt, val) -> {
            boolean enable = (boolean) val;
            stmt.setEscapeProcessing(enable);
        };
    }

    private static StatementAttributeSetter queryTimeoutSetter() {
        return (stmt, val) -> {
            int seconds = (int) val;
            stmt.setQueryTimeout(seconds);
        };
    }

    private static StatementAttributeSetter cursorNameSetter() {
        return (stmt, val) -> {
            String name = (String) val;
            stmt.setCursorName(name);
        };
    }

    private static StatementAttributeSetter fetchDirectionSetter() {
        return (stmt, val) -> {
            int direction = (int) val;
            stmt.setFetchDirection(direction);
        };
    }

    private static StatementAttributeSetter fetchSizeSetter() {
        return (stmt, val) -> {
            int rows = (int) val;
            stmt.setFetchSize(rows);
        };
    }

    private static StatementAttributeSetter poolableSetter() {
        return (stmt, val) -> {
            boolean poolable = (boolean) val;
            stmt.setPoolable(poolable);
        };
    }

    private static StatementAttributeSetter resultSetConcurrencySetter() {
        return (stmt, val) -> {
            // no-op
        };
    }

    private static StatementAttributeSetter resultSetHoldabilitySetter() {
        return (stmt, val) -> {
            // no-op
        };
    }

    private static StatementAttributeSetter resultSetTypeSetter() {
        return (stmt, val) -> {
            // no-op
        };
    }

}
