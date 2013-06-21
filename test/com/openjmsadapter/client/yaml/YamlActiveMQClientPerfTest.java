/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/client/yaml/YamlActiveMQClientPerfTest.java $
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

import com.openjmsadapter.client.yaml.ActiveMQClient;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class YamlActiveMQClientPerfTest {

    @Test
    public void testPublish10MessagesToQueueAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
            long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 10 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String text = client1.receiveBytesMessageFromQueueIntoString("test.queue");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 10 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

    @Test
    public void testPublish100MessagesToQueueAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
            long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 100 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String text = client1.receiveBytesMessageFromQueueIntoString("test.queue");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 100 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

    @Test
    public void testPublish1000MessagesToQueueAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[0].getLastSequenceSent();
            long sentSeq = client.publishToQueueAsBytes("HELLO", "test.queue");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 1000 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String text = client1.receiveBytesMessageFromQueueIntoString("test.queue");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 1000 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

    @Test
    public void testPublish10MessagesToTopicAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
            long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 10 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String text = client1.receiveBytesMessageFromTopicIntoString("test.topic");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 10 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

    @Test
    public void testPublish100MessagesToTopicAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
            long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 100 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String text = client1.receiveBytesMessageFromTopicIntoString("test.topic");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 100 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

    @Test
    public void testPublish1000MessagesToTopicAsBytes() throws OpenJMSAdapterException, FileNotFoundException {
        ActiveMQClient client1 = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config_1.yml");
        client1.connect();
        ActiveMQClient client = new ActiveMQClient("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        client.connect();
        long epoch = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            long lastSentSeq = client.getConfig().getDestinations()[1].getLastSequenceSent();
            long sentSeq = client.publishToTopicAsBytes("HELLO", "test.topic");
            assertTrue(sentSeq == lastSentSeq + 1 + i);
        }
        System.out.println("Published 1000 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client.disconnect();
        epoch = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String text = client1.receiveBytesMessageFromTopicIntoString("test.topic");
            assertTrue(text.equals("HELLO"));
        }
        System.out.println("Received 1000 mesasges in" + (epoch - System.currentTimeMillis()) + "ms");
        client1.disconnect();
    }

}
