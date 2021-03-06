/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse;

/**
 * author:liujun
 * Created:2016/9/16
 */
public interface ParseJsonServiceInf<T> {

    /**
     * parseBeanToJson
     *
     * @param
     * @return
     * @Created 2016/9/16
     */
    String parseBeanToJson(T t);

    /**
     * parseJsonToBean
     *
     * @param json
     * @return
     * @Created 2016/9/16
     */
    T parseJsonToBean(String json);

}
