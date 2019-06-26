/*
 * Copyright (C) 2016-2018 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.sharding.client.config.loader.zkprocess.parse.entryparse.schema.xml;

import com.sharding.client.alarm.AlarmCode;
import com.sharding.client.config.loader.zkprocess.entity.Schemas;
import com.sharding.client.config.loader.zkprocess.parse.ParseXmlServiceInf;
import com.sharding.client.config.loader.zkprocess.parse.XmlProcessBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * author:liujun
 * Created:2016/9/16
 */
public class SchemasParseXmlImpl implements ParseXmlServiceInf<Schemas> {


    private static final Logger LOGGER = LoggerFactory.getLogger(SchemasParseXmlImpl.class);

    private XmlProcessBase parseBean;

    public SchemasParseXmlImpl(XmlProcessBase parseBase) {

        this.parseBean = parseBase;
        parseBean.addParseClass(Schemas.class);
    }

    @Override
    public Schemas parseXmlToBean(String path) {

        Schemas schema = null;

        try {
            schema = (Schemas) this.parseBean.baseParseXmlToBean(path);
        } catch (JAXBException e) {
            LOGGER.warn(AlarmCode.CORE_ZK_WARN + "SchemasParseXmlImpl parseXmlToBean JAXBException", e);
        } catch (XMLStreamException e) {
            LOGGER.warn(AlarmCode.CORE_ZK_WARN + "SchemasParseXmlImpl parseXmlToBean XMLStreamException", e);
        }

        return schema;
    }

    @Override
    public void parseToXmlWrite(Schemas data, String outputFile, String dataName) {
        try {
            this.parseBean.baseParseAndWriteToXml(data, outputFile, dataName);
        } catch (IOException e) {
            LOGGER.warn(AlarmCode.CORE_ZK_WARN +
                    "SchemasParseXmlImpl parseToXmlWrite IOException", e);
        }
    }

}
