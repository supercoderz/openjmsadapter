/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/transport/ReceiverEndPointTest.java $
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
package com.openjmsadapter.transport;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class ReceiverEndPointTest {

    @Test
    public void testInitializeWithMessageConsumer() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        assertNotNull(new ReceiverEndpoint(consumer));
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveBytesMessageWithoutTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        BytesMessage message = session.createBytesMessage();
        message.writeBytes("HELLO".getBytes());
        pub.sendBytesMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        BytesMessage received = null;
        do{
            received = rec.receiveBytesMessageNoWait();
        }while(received==null);
        assertNotNull(received);
        byte[] bytes = new byte[5];
        received.readBytes(bytes);
        assertTrue((new String(bytes)).equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveBytesMessageWithTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        BytesMessage message = session.createBytesMessage();
        message.writeBytes("HELLO".getBytes());
        pub.sendBytesMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        BytesMessage received = rec.receiveBytesMessage(5000);
        assertNotNull(received);
        byte[] bytes = new byte[5];
        received.readBytes(bytes);
        assertTrue((new String(bytes)).equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveBytesMessageWithNoWait() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        BytesMessage message = session.createBytesMessage();
        message.writeBytes("HELLO".getBytes());
        pub.sendBytesMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        BytesMessage received = null;
        do{
            received = rec.receiveBytesMessageNoWait();
        }while(received==null);
        assertNotNull(received);
        byte[] bytes = new byte[5];
        received.readBytes(bytes);
        assertTrue((new String(bytes)).equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveTextMessageWithoutTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        TextMessage message = session.createTextMessage();
        message.setText("HELLO");
        pub.sendTextMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        TextMessage received=null;
        do{
            received = rec.receiveTextMessageNoWait();
        }while(received==null);
        assertNotNull(received);
        assertTrue(received.getText().equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveTextMessageWithTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        TextMessage message = session.createTextMessage();
        message.setText("HELLO");
        pub.sendTextMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        TextMessage received = rec.receiveTextMessage(5000);
        assertNotNull(received);
        assertTrue(received.getText().equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveTextMessageNoWait() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        TextMessage message = session.createTextMessage();
        message.setText("HELLO");
        pub.sendTextMessage(message);  
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        TextMessage received=null;
        do{
            received = rec.receiveTextMessageNoWait();
        }while(received==null);
        assertNotNull(received);
        assertTrue(received.getText().equals("HELLO"));
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveObjectMessageWithoutTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        ObjectMessage message = session.createObjectMessage();
        message.setObject(new Integer(1));
        pub.sendObjectMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        ObjectMessage received=null;
        do{
            received = rec.receiveObjectMessage();
        }while(received==null);
        assertNotNull(received);
        assertTrue((Integer)received.getObject()==1);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveObjectMessageWithTimeout() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        ObjectMessage message = session.createObjectMessage();
        message.setObject(new Integer(1));
        pub.sendObjectMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        ObjectMessage received=rec.receiveObjectMessage(5000);
        assertNotNull(received);
        assertTrue((Integer)received.getObject()==1);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testReceiveObjectMessageNoWait() throws Exception {
        Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(session.createQueue("test.queue"));
        PublisherEndpoint pub = new PublisherEndpoint(producer);
        ObjectMessage message = session.createObjectMessage();
        message.setObject(new Integer(1));
        pub.sendObjectMessage(message);
        MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
        ReceiverEndpoint rec = new ReceiverEndpoint(consumer);
        ObjectMessage received=null;
        do{
            received = rec.receiveObjectMessageNoWait();
        }while(received==null);
        assertNotNull(received);
        assertTrue((Integer)received.getObject()==1);
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    @Test(expected = JMSException.class)
    public void testEndpointDisconnect() throws JMSException {
        ReceiverEndpoint rec = null;
        ObjectMessage message = null;
        try {
            Connection connection = TestJMSUtil.getActiveMQConnection(TestJMSUtil.loadConfig("C:/NetBeansProjects/openjmsadapter/test/test_jms_config.yml"));
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            message = session.createObjectMessage();
            message.setObject(new Integer(1));
            MessageConsumer consumer = session.createConsumer(session.createQueue("test.queue"));
            rec = new ReceiverEndpoint(consumer);
        } catch (Exception e) {
            fail("Test setup failed due to " + e.getMessage());
        }
        if (rec != null && message != null) {
            rec.disconnect();
            rec.receiveBytesMessage();
        }
    }

}
