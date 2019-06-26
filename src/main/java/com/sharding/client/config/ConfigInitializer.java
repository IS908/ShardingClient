/*
 * Copyright (C) 2016-2018 ActionTech.
 * based on code by MyCATCopyrightHolder Copyright (c) 2013, OpenCloudDB/MyCAT.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */
package com.sharding.client.config;

import com.sharding.client.config.loader.SchemaLoader;
import com.sharding.client.config.loader.xml.XMLSchemaLoader;
import com.sharding.client.config.loader.xml.XMLServerLoader;
import com.sharding.client.config.model.*;
import com.sharding.client.config.util.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author mycat
 */
public class ConfigInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInitializer.class);

    private volatile SystemConfig system;
    private volatile FirewallConfig firewall;
    private volatile Map<String, UserConfig> users;
    private volatile Map<String, SchemaConfig> schemas;
    private volatile Map<String, DataNodeConfig> dataNodes;

    public Map<String, DataNodeConfig> getDataNodes() {
        return dataNodes;
    }

    public Map<String, DataHostConfig> getDataHosts() {
        return dataHosts;
    }

    private volatile Map<String, DataHostConfig> dataHosts;
    private volatile Map<ERTable, Set<ERTable>> erRelations;


    private volatile boolean dataHostWithoutWH = true;

    public ConfigInitializer(boolean lowerCaseNames) {
        XMLServerLoader serverLoader = new XMLServerLoader(lowerCaseNames);
        this.system = serverLoader.getSystem();
        this.users = serverLoader.getUsers();
        this.firewall = serverLoader.getFirewall();

        SchemaLoader schemaLoader = new XMLSchemaLoader(lowerCaseNames);
        this.dataHosts = initDataHosts(schemaLoader);
        this.dataNodes = initDataNodes(schemaLoader);
        this.schemas = schemaLoader.getSchemas();
        this.erRelations = schemaLoader.getErRelations();
        /* check config */
        this.selfChecking0();
    }

    private Map<String, DataHostConfig> initDataHosts(SchemaLoader schemaLoader) {
        Map<String, DataHostConfig> nodeConf = schemaLoader.getDataHosts();
        return nodeConf;
    }

    private Map<String, DataNodeConfig> initDataNodes(SchemaLoader schemaLoader) {
        Map<String, DataNodeConfig> nodeConf = schemaLoader.getDataNodes();
        return nodeConf;
    }

    private void selfChecking0() throws ConfigException {
        // check 1.user's schemas are all existed in schema's conf
        // 2.schema's conf is not empty
        if (users == null || users.isEmpty()) {
            throw new ConfigException("SelfCheck### user all node is empty!");
        } else {
            for (UserConfig uc : users.values()) {
                if (uc == null) {
                    throw new ConfigException("SelfCheck### users node within the item is empty!");
                }
                if (!uc.isManager()) {
                    Set<String> authSchemas = uc.getSchemas();
                    if (authSchemas == null) {
                        throw new ConfigException("SelfCheck### user " + uc.getName() + "referred schemas is empty!");
                    }
                    for (String schema : authSchemas) {
                        if (!schemas.containsKey(schema)) {
                            String errMsg = "SelfCheck###  schema " + schema + " referred by user " + uc.getName() + " is not exist!";
                            throw new ConfigException(errMsg);
                        }
                    }
                }
            }
        }


    }

    public SystemConfig getSystem() {
        return system;
    }


    public FirewallConfig getFirewall() {
        return firewall;
    }

    public Map<String, UserConfig> getUsers() {
        return users;
    }

    public Map<String, SchemaConfig> getSchemas() {
        return schemas;
    }

    public Map<ERTable, Set<ERTable>> getErRelations() {
        return erRelations;
    }


    public boolean isDataHostWithoutWH() {
        return dataHostWithoutWH;
    }

}
