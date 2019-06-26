/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse.entryparse.server.json;

import com.google.gson.reflect.TypeToken;
import com.sharding.client.config.loader.zkprocess.entity.server.User;
import com.sharding.client.config.loader.zkprocess.parse.JsonProcessBase;
import com.sharding.client.config.loader.zkprocess.parse.ParseJsonServiceInf;

import java.lang.reflect.Type;
import java.util.List;

/**
 * UserJsonParse
 * <p>
 * <p>
 * author:liujun
 * Created:2016/9/17
 */
public class UserJsonParse extends JsonProcessBase implements ParseJsonServiceInf<List<User>> {

    @Override
    public String parseBeanToJson(List<User> t) {
        return this.toJsonFromBean(t);
    }

    @Override
    public List<User> parseJsonToBean(String json) {
        Type parseType = new TypeToken<List<User>>() {
        }.getType();

        return this.toBeanformJson(json, parseType);
    }

}
