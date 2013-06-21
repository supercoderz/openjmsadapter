/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/client/yaml/YamlOpenMQClientTest.java $
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
package com.openjmsadapter.client.yaml;

import com.openjmsadapter.client.yaml.OpenMQClient;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class YamlOpenMQClientTest {

    @Test
    public void testCreateOpenMQClient() throws OpenJMSAdapterException, FileNotFoundException {
        assertNotNull(new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCannotCreateClientWithInvalidConfigFile() throws OpenJMSAdapterException, FileNotFoundException {
        assertNotNull(new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/invalid_yaml_config_2.yml"));
    }

    @Test
    public void testConnectDisconnectOpenMQClient() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        client.disconnect();
    }

    @Test
    public void testConnectDisconnectRepeatedly() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        //once
        client.connect();
        client.disconnect();
        //twice
        client.connect();
        client.disconnect();
        //thrice
        client.connect();
        client.disconnect();
        //onelast time
        client.connect();
        client.disconnect();
        //OK ..final time
        client.connect();
        client.disconnect();
    }

    @Test
    public void testConnectConnectedAndDisconnectDisconnected() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        client.connect();
        client.disconnect();
        client.disconnect();
    }

    @Test
    public void testReconnectWithDelay() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        client.reconnectWith(1000);
        client.disconnect();

    }

    @Test
    public void testPublishToQueueAsBytesAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = client1.receiveBytesMessageFromQueueIntoString("test.queue");
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToQueueAsBytesAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = client1.receiveBytesMessageFromQueueIntoString("test.queue", 1000);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToQueueAsBytesAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = null;
        do {
            text = client1.receiveBytesMessageFromQueueIntoStringNoWait("test.queue");
        } while (text == null);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToQueueAsTextAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsText("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = client1.receiveTextMessageFromQueueIntoString("test.queue");
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToQueueAsTextAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsText("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = client1.receiveTextMessageFromQueueIntoString("test.queue", 1000);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToQueueAsTextAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsText("HELLO", "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        String text = null;
        do {
            text = client1.receiveTextMessageFromQueueIntoStringNoWait("test.queue");
        } while (text == null);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToQueueAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsObject(new Integer(1000), "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        Integer a = (Integer) client1.receiveObjectMessageFromQueue("test.queue");
        assertTrue(a == 1000);
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToQueueAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsObject(new Integer(1000), "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        Integer a = (Integer) client1.receiveObjectMessageFromQueue("test.queue", 1000);
        assertTrue(a == 1000);
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToQueueAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
        long sentSeq = client.publishToQueueAsObject(new Integer(1000), "test.queue");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        Integer a = null;
        do {
            a = (Integer) client1.receiveObjectMessageFromQueueNoWait("test.queue");
        } while (a == null);
        assertTrue(a == 1000);
        client1.disconnect();
    }



    @Test
    public void testPublishToTopicAsBytesAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = client1.receiveBytesMessageFromTopicIntoString("test.topic");
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToTopicAsBytesAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = client1.receiveBytesMessageFromTopicIntoString("test.topic", 1000);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToTopicAsBytesAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = null;
        do {
            text = client1.receiveBytesMessageFromTopicIntoStringNoWait("test.topic");
        } while (text == null);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToTopicAsTextAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsText("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = client1.receiveTextMessageFromTopicIntoString("test.topic");
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToTopicAsTextAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsText("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = client1.receiveTextMessageFromTopicIntoString("test.topic", 1000);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishToTopicAsTextAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsText("HELLO", "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        String text = null;
        do {
            text = client1.receiveTextMessageFromTopicIntoStringNoWait("test.topic");
        } while (text == null);
        assertTrue(text.equals("HELLO"));
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToTopicAndReceive() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsObject(new Integer(1000), "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        Integer a = (Integer) client1.receiveObjectMessageFromTopic("test.topic");
        assertTrue(a == 1000);
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToTopicAndReceiveWithTimeout() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsObject(new Integer(1000), "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        Integer a = (Integer) client1.receiveObjectMessageFromTopic("test.topic", 1000);
        assertTrue(a == 1000);
        client1.disconnect();
    }

    @Test
    public void testPublishObjectToTopicAndReceiveNoWait() throws OpenJMSAdapterException, FileNotFoundException {
        OpenMQClient client1 = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_3.yml");
        client1.connect();
        OpenMQClient client = new OpenMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_2.yml");
        client.connect();
        long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
        long sentSeq = client.publishToTopicAsObject(new Integer(1000), "test.topic");
        assertTrue(sentSeq == lastSentSeq + 1);
        client.disconnect();
        Integer a = null;
        do {
            a = (Integer) client1.receiveObjectMessageFromTopicNoWait("test.topic");
        } while (a == null);
        assertTrue(a == 1000);
        client1.disconnect();
    }
}