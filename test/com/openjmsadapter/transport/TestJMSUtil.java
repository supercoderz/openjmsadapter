/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/transport/TestJMSUtil.java $
 * $Revision: 29 $
 * $Date: 2010-01-10 07:28:43 +0000 (Sun, 10 Jan 2010) $
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

package com.openjmsadapter.transport;

import java.io.File;
import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ho.yaml.Yaml;

/**
 *
 * @author hari
 */
public class TestJMSUtil {
    public static TestJMSConfig loadConfig(String filename) throws Exception{
        return (TestJMSConfig) Yaml.loadType(new File(filename),TestJMSConfig.class);
    }

    public static Connection getActiveMQConnection(TestJMSConfig config)throws Exception{
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(config.getJMSConnectionUser(),config.getJMSConnectionPassword(),config.getJMSConnectionUrl());
        return factory.createConnection();
    }

      

}
