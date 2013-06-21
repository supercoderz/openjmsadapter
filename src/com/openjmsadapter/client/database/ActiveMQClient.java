/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/client/database/ActiveMQClient.java $
 * $Revision: 35 $
 * $Date: 2010-01-23 21:30:56 +0000 (Sat, 23 Jan 2010) $
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
package com.openjmsadapter.client.database;

import com.openjmsadapter.client.base.DatabaseConfigClient;
import com.openjmsadapter.client.base.YamlConfigClient;
import com.openjmsadapter.connection.ActiveMQConnectionFactoryBuilder;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import java.io.FileNotFoundException;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class ActiveMQClient extends DatabaseConfigClient {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private ActiveMQConnectionFactoryBuilder connectionFactoryBuilder;

    public ActiveMQClient(String configFileName) {
        super(configFileName);
    }

    public ActiveMQClient() {
        super();
    }

    public void connect() throws OpenJMSAdapterException {
        logger.info("Creating connection factory");
        connectionFactoryBuilder = new ActiveMQConnectionFactoryBuilder();
        logger.info("Completed creating connection factory.");
        connectUsingFactory(connectionFactoryBuilder.getConnectionFactory(getConfig()));
    }

    public void disconnect() throws OpenJMSAdapterException {
        disconnectJMSConnection();
    }

    public void reconnectWith(long timeout) throws OpenJMSAdapterException {
        delayedReconnect(timeout);
    }
    
}
