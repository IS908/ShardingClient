package com.sharding.client.jdbc.api;

import java.sql.SQLException;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
@FunctionalInterface
public interface ParameterSetter {

    void setParameter(int index, Object value) throws SQLException;

}
