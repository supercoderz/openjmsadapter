/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/session/TopicPublishSessionTest.java $
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
package com.openjmsadapter.session;

import com.openjmsadapter.configuration.yaml.YamlConfigurationHolder;
import com.openjmsadapter.connection.ActiveMQConnectionFactoryBuilder;
import com.openjmsadapter.connection.OpenJMSAdapterConnection;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import java.io.FileNotFoundException;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class TopicPublishSessionTest {

    @Test
    public void testCreateTopicPublishSession() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        assertNotNull(connection.getTopicPublishSession("test.topic"));
        assertTrue(connection.getTopicPublishSession("test.topic") instanceof TopicPublishSession);
        connection.disconnect();
    }

    @Test
    public void testConnectConnectedQueuePublishSession() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        //session gets connected here
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        //try to connect the session again here - this should not fail
        connection.getTopicPublishSession("test.topic").connect();
        connection.disconnect();
    }

    @Test
    public void testDiconnectDisconnectedQueuePublishSession() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        //session gets connected here
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        connection.disconnect();
        session.disconnect();
    }

    @Test
    public void testCreateBytesMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        BytesMessage msg = session.createBytesMessage();
        assertNotNull(msg);
        connection.disconnect();
    }

    @Test
    public void testCreateTextMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        TextMessage msg = session.createTextMessage();
        assertNotNull(msg);
        connection.disconnect();
    }

    @Test
    public void testCreateObjectMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        ObjectMessage msg = session.createObjectMessage();
        assertNotNull(msg);
        connection.disconnect();
    }

    @Test
    public void testPublishBytesMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        Connection conn = builder.getConnectionFactory(holder.getConfig()).createConnection();
        conn.start();
        Session receive_session=conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = receive_session.createConsumer(receive_session.createTopic("test.topic"));
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        BytesMessage msg = session.createBytesMessage();
        msg.writeBytes("HELLO".getBytes());
        session.publish(msg);
        connection.disconnect();
        BytesMessage rx_msg = (BytesMessage) consumer.receive();
        assertNotNull(rx_msg);
        byte[] bytes= new byte[5];
        rx_msg.readBytes(bytes);
        assertTrue((new String(bytes)).equals("HELLO"));
        consumer.close();
        receive_session.close();
        conn.close();
    }

    @Test
    public void testPublishTextMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        Connection conn = builder.getConnectionFactory(holder.getConfig()).createConnection();
        conn.start();
        Session receive_session=conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = receive_session.createConsumer(receive_session.createTopic("test.topic"));
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        TextMessage msg = session.createTextMessage();
        msg.setText("HELLO");
        session.publish(msg);
        connection.disconnect();
        TextMessage rx_msg = (TextMessage) consumer.receive();
        assertNotNull(rx_msg);
        assertTrue(rx_msg.getText().equals("HELLO"));
        consumer.close();
        receive_session.close();
        conn.close();
    }

    @Test
    public void testPublishObjectMessage() throws FileNotFoundException,JMSException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        Connection conn = builder.getConnectionFactory(holder.getConfig()).createConnection();
        conn.start();
        Session receive_session=conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = receive_session.createConsumer(receive_session.createTopic("test.topic"));
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createTopicPublishingSession("test.topic");
        TopicPublishSession session = (TopicPublishSession) connection.getTopicPublishSession("test.topic");
        ObjectMessage msg = session.createObjectMessage();
        msg.setObject(new Integer(1));
        session.publish(msg);
        connection.disconnect();
        ObjectMessage rx_msg = (ObjectMessage) consumer.receive();
        assertNotNull(rx_msg);
        assertTrue((Integer)(rx_msg.getObject())==1);
        consumer.close();
        receive_session.close();
        conn.close();
    }

}