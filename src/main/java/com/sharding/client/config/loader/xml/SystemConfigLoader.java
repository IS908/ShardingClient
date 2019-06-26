/*
 * Copyright (C) 2016-2018 ActionTech.
 * based on code by MyCATCopyrightHolder Copyright (c) 2013, OpenCloudDB/MyCAT.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */
package com.sharding.client.config.loader.xml;

import com.sharding.client.config.Versions;
import com.sharding.client.config.model.SystemConfig;
import com.sharding.client.config.util.ConfigException;
import com.sharding.client.config.util.ConfigUtil;
import com.sharding.client.config.util.ParameterMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class SystemConfigLoader implements Loader<SystemConfig, XMLServerLoader> {
    public void load(Element root, XMLServerLoader xsl, boolean isLowerCaseTableNames) throws IllegalAccessException, InvocationTargetException {
        SystemConfig system = xsl.getSystem();
        NodeList list = root.getElementsByTagName("system");

        for (int i = 0, n = list.getLength(); i < n; i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                Map<String, Object> props = ConfigUtil.loadElements((Element) node);
                ParameterMapping.mapping(system, props);
            }
        }

        if (system.getFakeMySQLVersion() != null) {
            boolean validVersion = false;
            String majorMySQLVersion = system.getFakeMySQLVersion();
            int pos = majorMySQLVersion.indexOf(".") + 1;
            majorMySQLVersion = majorMySQLVersion.substring(0, majorMySQLVersion.indexOf(".", pos));
            for (String ver : SystemConfig.MYSQL_VERSIONS) {
                // version is x.y.z ,just compare the x.y
                if (majorMySQLVersion.equals(ver)) {
                    validVersion = true;
                }
            }

            if (validVersion) {
                Versions.setServerVersion(system.getFakeMySQLVersion());
            } else {
                throw new ConfigException("The specified MySQL Version (" + system.getFakeMySQLVersion() + ") is not valid.");
            }
        }
    }
}
