/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse.entryparse.schema.json;

import com.google.gson.reflect.TypeToken;
import com.sharding.client.config.loader.zkprocess.entity.schema.datahost.DataHost;
import com.sharding.client.config.loader.zkprocess.parse.JsonProcessBase;
import com.sharding.client.config.loader.zkprocess.parse.ParseJsonServiceInf;

import java.lang.reflect.Type;
import java.util.List;

/**
 * DataHostJsonParse
 * <p>
 * <p>
 * author:liujun
 * Created:2016/9/17
 */
public class DataHostJsonParse extends JsonProcessBase implements ParseJsonServiceInf<List<DataHost>> {

    @Override
    public String parseBeanToJson(List<DataHost> t) {
        return this.toJsonFromBean(t);
    }

    @Override
    public List<DataHost> parseJsonToBean(String json) {
        Type parseType = new TypeToken<List<DataHost>>() {
        }.getType();

        return this.toBeanformJson(json, parseType);
    }

}
