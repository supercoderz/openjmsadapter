/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/testsuite/OpenJmsAdapterTestSuite.java $
 * $Revision: 36 $
 * $Date: 2010-01-28 15:00:03 +0000 (Thu, 28 Jan 2010) $
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
package com.openjmsadapter.testsuite;

import com.openjmsadapter.client.database.DatabaseActiveMQClientTest;
import com.openjmsadapter.client.database.DatabaseOpenMQClientTest;
import com.openjmsadapter.client.yaml.YamlActiveMQClientTest;
import com.openjmsadapter.client.yaml.YamlOpenMQClientTest;
import com.openjmsadapter.configuration.DatabaseConfigHolderTest;
import com.openjmsadapter.configuration.YamlConfigurationHolderTest;
import com.openjmsadapter.connection.ActiveMQConnectionFactoryBuilderTest;
import com.openjmsadapter.connection.OpenJMSAdapterConnectionTest;
import com.openjmsadapter.session.QueuePublishSessionTest;
import com.openjmsadapter.session.QueueReceiveSessionTest;
import com.openjmsadapter.session.TopicPublishSessionTest;
import com.openjmsadapter.session.TopicReceiveSessionTest;
import com.openjmsadapter.transport.PublisherEndpointTest;
import com.openjmsadapter.transport.ReceiverEndPointTest;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author hari
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PublisherEndpointTest.class,
    ReceiverEndPointTest.class,
    YamlConfigurationHolderTest.class,
    ActiveMQConnectionFactoryBuilderTest.class,
    OpenJMSAdapterConnectionTest.class,
    QueuePublishSessionTest.class,
    TopicPublishSessionTest.class,
    QueueReceiveSessionTest.class,
    TopicReceiveSessionTest.class,
    YamlActiveMQClientTest.class,
    YamlOpenMQClientTest.class,
    DatabaseConfigHolderTest.class,
    DatabaseActiveMQClientTest.class,
    DatabaseOpenMQClientTest.class
})
public class OpenJmsAdapterTestSuite {

    @BeforeClass
    public static void loadLog4J() {
        PropertyConfigurator.configure("log4j.properties");
    }
}
