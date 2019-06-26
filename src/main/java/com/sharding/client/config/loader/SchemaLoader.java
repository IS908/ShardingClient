/*
 * Copyright (C) 2016-2018 ActionTech.
 * based on code by MyCATCopyrightHolder Copyright (c) 2013, OpenCloudDB/MyCAT.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */
package com.sharding.client.config.loader;

import com.sharding.client.config.model.DataHostConfig;
import com.sharding.client.config.model.DataNodeConfig;
import com.sharding.client.config.model.ERTable;
import com.sharding.client.config.model.SchemaConfig;

import java.util.Map;
import java.util.Set;

/**
 * @author mycat
 */
public interface SchemaLoader {

    Map<String, DataHostConfig> getDataHosts();

    Map<String, DataNodeConfig> getDataNodes();

    Map<String, SchemaConfig> getSchemas();

    Map<ERTable, Set<ERTable>> getErRelations();
}
