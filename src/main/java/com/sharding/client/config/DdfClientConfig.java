/*
 * Copyright (C) 2016-2018 ActionTech.
 * based on code by MyCATCopyrightHolder Copyright (c) 2013, OpenCloudDB/MyCAT.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */
package com.sharding.client.config;

import com.sharding.client.config.model.*;
import com.sharding.client.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * ddf client config
 */
public class DdfClientConfig {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DdfClientConfig.class);

    private static final DdfClientConfig INSTANCE = new DdfClientConfig();

    public static DdfClientConfig getInstance() {
        return INSTANCE;
    }

    private static final int RELOAD = 1;
    private static final int ROLLBACK = 2;
    private static final int RELOAD_ALL = 3;

    private volatile SystemConfig system;
    private volatile FirewallConfig firewall;
    private volatile FirewallConfig firewall2;
    private volatile Map<String, UserConfig> users;
    private volatile Map<String, UserConfig> users2;
    private volatile Map<String, SchemaConfig> schemas;
    private volatile Map<String, SchemaConfig> schemas2;

    public Map<String, DataNodeConfig> getDataNodes() {
        return dataNodes;
    }

    public Map<String, DataNodeConfig> getDataNodes2() {
        return dataNodes2;
    }

    public Map<String, DataHostConfig> getDataHosts() {
        return dataHosts;
    }

    public Map<String, DataHostConfig> getDataHosts2() {
        return dataHosts2;
    }

    private volatile Map<String, DataNodeConfig> dataNodes;
    private volatile Map<String, DataNodeConfig> dataNodes2;
    private volatile Map<String, DataHostConfig> dataHosts;
    private volatile Map<String, DataHostConfig> dataHosts2;
    private volatile Map<ERTable, Set<ERTable>> erRelations;
    private volatile Map<ERTable, Set<ERTable>> erRelations2;
    private volatile boolean dataHostWithoutWR;
    private volatile boolean dataHostWithoutWR2;
    private volatile long reloadTime;
    private volatile long rollbackTime;
    private volatile int status;
    private volatile boolean changing = false;

    public DdfClientConfig() {
        //read schema.xml,rule.xml and server.xml
        ConfigInitializer confInit = new ConfigInitializer(false);
        this.system = confInit.getSystem();
        this.users = confInit.getUsers();
        this.schemas = confInit.getSchemas();
        this.erRelations = confInit.getErRelations();
        this.dataHostWithoutWR = confInit.isDataHostWithoutWH();
        this.dataHosts = confInit.getDataHosts();
        this.dataNodes = confInit.getDataNodes();
        this.firewall = confInit.getFirewall();

        this.reloadTime = TimeUtil.currentTimeMillis();
        this.rollbackTime = -1L;
        this.status = RELOAD;
    }

    private void waitIfChanging() {
        while (changing) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
    }

    public SystemConfig getSystem() {
        waitIfChanging();
        return system;
    }

    public boolean isDataHostWithoutWR() {
        waitIfChanging();
        return dataHostWithoutWR;
    }

    public Map<String, UserConfig> getUsers() {
        waitIfChanging();
        return users;
    }

    public Map<String, UserConfig> getBackupUsers() {
        waitIfChanging();
        return users2;
    }

    public Map<String, SchemaConfig> getSchemas() {
        waitIfChanging();
        return schemas;
    }

    public Map<String, SchemaConfig> getBackupSchemas() {
        waitIfChanging();
        return schemas2;
    }

    public Map<ERTable, Set<ERTable>> getErRelations() {
        waitIfChanging();
        return erRelations;
    }

    public Map<ERTable, Set<ERTable>> getBackupErRelations() {
        waitIfChanging();
        return erRelations2;
    }

    public FirewallConfig getFirewall() {
        waitIfChanging();
        return firewall;
    }

    public FirewallConfig getBackupFirewall() {
        waitIfChanging();
        return firewall2;
    }

    public long getReloadTime() {
        waitIfChanging();
        return reloadTime;
    }

    public long getRollbackTime() {
        waitIfChanging();
        return rollbackTime;
    }

    public boolean backDataHostWithoutWR() {
        waitIfChanging();
        return dataHostWithoutWR2;
    }


}


