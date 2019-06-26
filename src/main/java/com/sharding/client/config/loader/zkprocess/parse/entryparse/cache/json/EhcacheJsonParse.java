/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse.entryparse.cache.json;

import com.sharding.client.config.loader.zkprocess.entity.cache.Ehcache;
import com.sharding.client.config.loader.zkprocess.parse.JsonProcessBase;
import com.sharding.client.config.loader.zkprocess.parse.ParseJsonServiceInf;

/**
 * EhcacheJsonParse
 * <p>
 * <p>
 * author:liujun
 * Created:2016/9/17
 */
public class EhcacheJsonParse extends JsonProcessBase implements ParseJsonServiceInf<Ehcache> {

    @Override
    public String parseBeanToJson(Ehcache t) {
        return this.toJsonFromBean(t);
    }

    @Override
    public Ehcache parseJsonToBean(String json) {
        return this.toBeanformJson(json, Ehcache.class);
    }

}
