package com.sharding.client.jdbc.api;

import java.util.Map;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public interface DDFProxy {

    int getAttributeSize();

    void putAttribute(String k, Object v);

    Object getAttribute(String k);

    Map<String, Object> getAttributes();

    void clearAttributes();

}
