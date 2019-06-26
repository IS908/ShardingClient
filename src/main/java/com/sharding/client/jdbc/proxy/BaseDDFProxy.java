package com.sharding.client.jdbc.proxy;

import com.sharding.client.jdbc.api.DDFProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: // TODO
 * @Auther: kun
 * @Date: 2019-04-02
 */
public abstract class BaseDDFProxy implements DDFProxy {

    private Map<String, Object> attributeMap;

    public BaseDDFProxy(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public BaseDDFProxy() {
        this.attributeMap = new HashMap<>();
    }

    @Override
    public int getAttributeSize() {
        return attributeMap.size();
    }

    @Override
    public void putAttribute(String k, Object v) {
        attributeMap.put(k, v);
    }

    @Override
    public Object getAttribute(String k) {
        return attributeMap.get(k);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributeMap;
    }

    @Override
    public void clearAttributes() {
        attributeMap.clear();
    }

}
