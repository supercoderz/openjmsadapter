/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/session/QueueReceiveSessionTest.java $
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
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class QueueReceiveSessionTest {

    @Test
    public void testCreateQueueReceiveSession() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        connection.connect();
        connection.createQueueReceivingSession("test.queue");
        assertNotNull(connection.getQueueReceiveSession("test.queue"));
        assertTrue(connection.getQueueReceiveSession("test.queue") instanceof QueueReceiveSession);
        connection.disconnect();
    }

    @Test
    public void testConnectConnectedQueueReceiveSession() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        //session gets connected here
        connection.connect();
        connection.createQueueReceivingSession("test.queue");
        //try to connect the session again here - this should not fail
        connection.getQueueReceiveSession("test.queue").connect();
        connection.disconnect();
    }

    @Test
    public void testDiconnectDisconnectedQueueReceiveSession() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        OpenJMSAdapterConnection connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        //session gets connected here
        connection.connect();
        connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession session = (QueueReceiveSession) connection.getQueueReceiveSession("test.queue");
        connection.disconnect();
        session.disconnect();
    }

    @Test
    public void testReceiveBytesMessage() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        BytesMessage pub = pub_session.createBytesMessage();
        pub.writeBytes("HELLO".getBytes());
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        BytesMessage rx = rx_session.receiveBytesMessage();
        assertNotNull(rx);
        byte[] bytes = new byte[5];
        rx.readBytes(bytes);
        assertTrue("HELLO".equals(new String(bytes)));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveBytesMessageWithTimeout() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        BytesMessage pub = pub_session.createBytesMessage();
        pub.writeBytes("HELLO".getBytes());
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        BytesMessage rx = rx_session.receiveBytesMessage(5000);
        assertNotNull(rx);
        byte[] bytes = new byte[5];
        rx.readBytes(bytes);
        assertTrue("HELLO".equals(new String(bytes)));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveBytesMessageNoWait() throws FileNotFoundException, JMSException, InterruptedException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        BytesMessage pub = pub_session.createBytesMessage();
        pub.writeBytes("HELLO".getBytes());
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        BytesMessage rx = null;
        do {
            rx = rx_session.receiveBytesMessageNoWait();
        } while (rx == null);
        assertNotNull(rx);
        byte[] bytes = new byte[5];
        rx.readBytes(bytes);
        assertTrue("HELLO".equals(new String(bytes)));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveTextMessage() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        TextMessage pub = pub_session.createTextMessage();
        pub.setText("HELLO");
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        TextMessage rx = rx_session.receiveTextMessage();
        assertNotNull(rx);
        assertTrue("HELLO".equals(rx.getText()));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveTextMessageWithTimeout() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        TextMessage pub = pub_session.createTextMessage();
        pub.setText("HELLO");
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        TextMessage rx = rx_session.receiveTextMessage(5000);
        assertNotNull(rx);
        assertTrue("HELLO".equals(rx.getText()));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveTextMessageNoWait() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        TextMessage pub = pub_session.createTextMessage();
        pub.setText("HELLO");
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        TextMessage rx = null;
        do {
            rx = rx_session.receiveTextMessageNoWait();
        } while (rx == null);
        assertNotNull(rx);
        assertTrue("HELLO".equals(rx.getText()));
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveObjectMessage() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        ObjectMessage pub = pub_session.createObjectMessage();
        pub.setObject(new Integer(1));
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        ObjectMessage rx = rx_session.receiveObjectMessage();
        assertNotNull(rx);
        assertTrue((Integer) rx.getObject() == 1);
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveObjectMessageWithTimeout() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        ObjectMessage pub = pub_session.createObjectMessage();
        pub.setObject(new Integer(1));
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        ObjectMessage rx = rx_session.receiveObjectMessage(5000);
        assertNotNull(rx);
        assertTrue((Integer) rx.getObject() == 1);
        rx.acknowledge();
        rx_connection.disconnect();
    }

    @Test
    public void testReceiveObjectMessageNoWait() throws FileNotFoundException, JMSException, OpenJMSAdapterException {
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        //Publish
        OpenJMSAdapterConnection pub_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        pub_connection.connect();
        pub_connection.createQueuePublishingSession("test.queue");
        QueuePublishSession pub_session = (QueuePublishSession) pub_connection.getQueuePublishSession("test.queue");
        ObjectMessage pub = pub_session.createObjectMessage();
        pub.setObject(new Integer(1));
        pub_session.publish(pub);
        pub_connection.disconnect();
        //Receive
        OpenJMSAdapterConnection rx_connection = new OpenJMSAdapterConnection(builder.getConnectionFactory(holder.getConfig()));
        rx_connection.connect();
        rx_connection.createQueueReceivingSession("test.queue");
        QueueReceiveSession rx_session = (QueueReceiveSession) rx_connection.getQueueReceiveSession("test.queue");
        ObjectMessage rx = null;
        do {
            rx = rx_session.receiveObjectMessageNoWait();
        } while (rx == null);
        assertNotNull(rx);
        assertTrue((Integer) rx.getObject() == 1);
        rx.acknowledge();
        rx_connection.disconnect();
    }

}
