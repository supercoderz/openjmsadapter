/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/connection/ActiveMQConnectionFactoryBuilder.java $
 * $Revision: 34 $
 * $Date: 2010-01-23 21:26:55 +0000 (Sat, 23 Jan 2010) $
 */
/*
 * This file is part of OpenJMSAdapter (openjmsadapter.sourceforge.net) Copyright (C) 2009 Narahari
 * Allamraju (anarahari@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.openjmsadapter.connection;

import com.openjmsadapter.configuration.AdapterConfiguration;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class ActiveMQConnectionFactoryBuilder implements ConnectionFactoryBuilder {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private ConnectionFactory connectionFactory;

    public ActiveMQConnectionFactoryBuilder() {
        //
    }

    public ConnectionFactory getConnectionFactory(AdapterConfiguration config) throws OpenJMSAdapterException{
        try{
        if (connectionFactory == null) {
            logger.debug("Creating ActiveMQConnection factory with Yaml configuration");
            connectionFactory = new ActiveMQConnectionFactory(config.getJMSConnectionUser(), config.getJMSConnectionPassword(), config.getJMSConnectionUrl());
            logger.debug("Completed creating ActiveMQConnection factory with Yaml configuration");
            return connectionFactory;
        } else {
            logger.debug("Returning already existing ActiveMQConnectionFactory");
            return connectionFactory;
        }
        }catch(Exception e){
            throw new OpenJMSAdapterException("Error creating ActiveMQConnectionFactory", e);
        }
    }
}
