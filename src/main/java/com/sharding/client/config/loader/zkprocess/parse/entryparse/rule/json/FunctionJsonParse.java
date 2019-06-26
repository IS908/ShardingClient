/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse.entryparse.rule.json;


import com.google.common.reflect.TypeToken;
import com.sharding.client.config.loader.zkprocess.entity.rule.function.Function;
import com.sharding.client.config.loader.zkprocess.parse.JsonProcessBase;
import com.sharding.client.config.loader.zkprocess.parse.ParseJsonServiceInf;

import java.lang.reflect.Type;
import java.util.List;

/**
 * FunctionJsonParse
 * <p>
 * <p>
 * author:liujun
 * Created:2016/9/17
 */
public class FunctionJsonParse extends JsonProcessBase implements ParseJsonServiceInf<List<Function>> {

    @Override
    public String parseBeanToJson(List<Function> t) {
        return this.toJsonFromBean(t);
    }

    @Override
    public List<Function> parseJsonToBean(String json) {

        Type parseType = new TypeToken<List<Function>>() {
        }.getType();

        return this.toBeanformJson(json, parseType);
    }

}
