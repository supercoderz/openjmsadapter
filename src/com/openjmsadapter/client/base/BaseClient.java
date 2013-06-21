/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/client/base/BaseClient.java $
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
package com.openjmsadapter.client.base;

import com.openjmsadapter.configuration.AdapterConfiguration;
import com.openjmsadapter.configuration.ConfigurationHolder;
import com.openjmsadapter.configuration.DestinationConfiguration;
import com.openjmsadapter.connection.OpenJMSAdapterConnection;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import com.openjmsadapter.exception.Utils;
import com.openjmsadapter.session.OpenJMSAdapterPublisher;
import com.openjmsadapter.session.QueuePublishSession;
import com.openjmsadapter.session.QueueReceiveSession;
import com.openjmsadapter.session.TopicPublishSession;
import com.openjmsadapter.session.TopicReceiveSession;
import java.io.Serializable;
import java.util.HashMap;
import javax.jms.BytesMessage;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public abstract class BaseClient {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final ConfigurationHolder configHolder;
    private OpenJMSAdapterConnection connection;
    private SequenceManager manager = new SequenceManager();

    public BaseClient(ConfigurationHolder configHolder) {
        this.configHolder = configHolder;
    }

    public abstract void connect() throws OpenJMSAdapterException;

    public void connectUsingFactory(ConnectionFactory connectionFactory) throws OpenJMSAdapterException {
        try {
            logger.info("Connecting ActiveMQClient");
            logger.info("Creating connection from connection factory");
            connection = new OpenJMSAdapterConnection(connectionFactory);
            logger.info("Establishing connection to the sessions");
            connection.connect();
            createSessions();
            logger.info("Connected ActiveMQClient");
        } catch (JMSException jmse) {
            logger.error("Encounterted exception " + jmse.getMessage() + " while connecting ActiveMQClient with the configurationfile " + configHolder.getFilename() + "\n Exception stack trace is:+\n" + Utils.getStacktraceAsString(jmse));
            throw new OpenJMSAdapterException("Error conecting ActiveMQClient", jmse);
        }
    }

    public void connectUsingFactoryWithUsernamePassword(ConnectionFactory connectionFactory) throws OpenJMSAdapterException {
        try {
            logger.info("Connecting ActiveMQClient");
            logger.info("Creating connection from connection factory");
            connection = new OpenJMSAdapterConnection(connectionFactory);
            logger.info("Establishing connection to the sessions");
            connection.connect(configHolder.getConfig().getJMSConnectionUser(), configHolder.getConfig().getJMSConnectionPassword());
            createSessions();
            logger.info("Connected ActiveMQClient");
        } catch (JMSException jmse) {
            logger.error("Encounterted exception " + jmse.getMessage() + " while connecting ActiveMQClient with the configurationfile " + configHolder.getFilename() + "\n Exception stack trace is:+\n" + Utils.getStacktraceAsString(jmse));
            throw new OpenJMSAdapterException("Error conecting ActiveMQClient", jmse);
        }
    }

    void createSessions() throws JMSException {
        for (DestinationConfiguration destinationConfig : configHolder.getConfig().getDestinations()) {
            if (destinationConfig.getDestinationType().equalsIgnoreCase("queue")) {
                if (destinationConfig.getOperationType().equalsIgnoreCase("publish")) {
                    connection.createQueuePublishingSession(destinationConfig.getDestinantionName());
                    manager.addDestination(destinationConfig.getDestinantionName(), destinationConfig.getLastSequenceSent());
                } else if (destinationConfig.getOperationType().equalsIgnoreCase("receive")) {
                    connection.createQueueReceivingSession(destinationConfig.getDestinantionName());
                    manager.addDestination(destinationConfig.getDestinantionName(), destinationConfig.getLastSequenceReceived());
                }
            } else if (destinationConfig.getDestinationType().equalsIgnoreCase("topic")) {
                if (destinationConfig.getOperationType().equalsIgnoreCase("publish")) {
                    connection.createTopicPublishingSession(destinationConfig.getDestinantionName());
                    manager.addDestination(destinationConfig.getDestinantionName(), destinationConfig.getLastSequenceSent());
                } else if (destinationConfig.getOperationType().equalsIgnoreCase("receive")) {
                    connection.createTopicReceivingSession(destinationConfig.getDestinantionName());
                    manager.addDestination(destinationConfig.getDestinantionName(), destinationConfig.getLastSequenceReceived());
                }
            }
        }
    }

    public void delayedReconnect(long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Reconnecting client with delay " + timeout);
            disconnect();
            logger.info("Disconnected client. Sleeping for " + timeout + "ms");
            Thread.sleep(timeout);
            logger.info("Attempting to connect again");
            connect();
            logger.info("Reconnected client with delay " + timeout);
        } catch (InterruptedException ie) {
            logger.error("Encounterted exception while reconencting ActiveMQClient with delay " + timeout + ". Exception stack traceis " + Utils.getStacktraceAsString(ie));
            throw new OpenJMSAdapterException("Error reconnecting with delay " + timeout, ie);
        }
    }

    public abstract void disconnect() throws OpenJMSAdapterException;

    public void disconnectJMSConnection() throws OpenJMSAdapterException {
        try {
            logger.info("Disconnecting ActiveMQClient");
            connection.disconnect();
            persistConfiguration();
            logger.info("Disconnected AcriveMQClient");
        } catch (Exception jmse) {
            logger.error("Encounterted exception " + jmse.getMessage() + " while disconnecting ActiveMQClient with the configurationfile " + configHolder.getFilename() + "\n Exception stack trace is:+\n" + Utils.getStacktraceAsString(jmse));
            throw new OpenJMSAdapterException("Error disconnecting ActiveMQClient", jmse);
        }
    }

    public AdapterConfiguration getConfig() {
        return configHolder.getConfig();
    }

    public void persistConfiguration() throws Exception {
        for (DestinationConfiguration config : configHolder.getConfig().getDestinations()) {
            if (config.getOperationType().equalsIgnoreCase("publish")) {
                config.setLastSequenceSent(manager.getLastSequence(config.getDestinantionName()));
            } else if (config.getOperationType().equalsIgnoreCase("receive")) {
                config.setLastSequenceReceived(manager.getLastSequence(config.getDestinantionName()));
            }
        }
        configHolder.persistConfiguration();
    }

    public long publishToQueueAsBytes(String message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue publish session for destination " + destinationName);
            QueuePublishSession session = connection.getQueuePublishSession(destinationName);
            if (session != null) {
                return publishAsBytesMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as bytes on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as bytes", e);
        }
    }

    public long publishToQueueAsObject(Serializable message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue publish session for destination " + destinationName);
            QueuePublishSession session = connection.getQueuePublishSession(destinationName);
            if (session != null) {
                return publishAsObjectMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as text on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as text", e);
        }
    }

    public long publishToQueueAsText(String message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue publish session for destination " + destinationName);
            QueuePublishSession session = connection.getQueuePublishSession(destinationName);
            if (session != null) {
                return publishAsTextMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as text on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as text", e);
        }
    }

    public long publishToTopicAsBytes(String message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic publish session for destination " + destinationName);
            TopicPublishSession session = connection.getTopicPublishSession(destinationName);
            if (session != null) {
                return publishAsBytesMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as bytes on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as bytes", e);
        }
    }

    public long publishToTopicAsObject(Serializable message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic publish session for destination " + destinationName);
            TopicPublishSession session = connection.getTopicPublishSession(destinationName);
            if (session != null) {
                return publishAsObjectMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as text on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as text", e);
        }
    }

    public long publishToTopicAsText(String message, String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic publish session for destination " + destinationName);
            TopicPublishSession session = connection.getTopicPublishSession(destinationName);
            if (session != null) {
                return publishAsTextMessage(destinationName, session, message);
            } else {
                logger.warn("No queue publish session found for destination " + destinationName);
                return -1;
            }
        } catch (Exception e) {
            logger.error("Encountered exception trying to publish message as text on " + destinationName);
            logger.error(Utils.getStacktraceAsString(e));
            throw new OpenJMSAdapterException("Error publishing message as text", e);
        }
    }

    public String receiveBytesMessageFromQueueIntoString(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName);
                BytesMessage message = session.receiveBytesMessage();
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public String receiveBytesMessageFromQueueIntoString(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName + " with timeout " + timeout + "ms");
                BytesMessage message = session.receiveBytesMessage(timeout);
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public String receiveBytesMessageFromQueueIntoStringNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName + " with no wait");
                BytesMessage message = session.receiveBytesMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public String receiveBytesMessageFromTopicIntoString(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName);
                BytesMessage message = session.receiveBytesMessage();
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public String receiveBytesMessageFromTopicIntoString(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName + " with timeout " + timeout + "ms");
                BytesMessage message = session.receiveBytesMessage(timeout);
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public String receiveBytesMessageFromTopicIntoStringNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive bytes message from destination " + destinationName + " with no wait");
                BytesMessage message = session.receiveBytesMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received bytes message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                byte[] bytes = new byte[(int) message.getBodyLength()];
                message.readBytes(bytes);
                logger.info("Read " + bytes.length + " bytes from message, converting to string");
                String retVal = new String(bytes);
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive bytes message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving bytes message as a string", e);
        }
    }

    public Serializable receiveObjectMessageFromQueue(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName);
                ObjectMessage message = session.receiveObjectMessage();
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public Serializable receiveObjectMessageFromQueue(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName + " with timeout " + timeout + "ms");
                ObjectMessage message = session.receiveObjectMessage(timeout);
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public Serializable receiveObjectMessageFromQueueNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName + " with no wait");
                ObjectMessage message = session.receiveObjectMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public Serializable receiveObjectMessageFromTopic(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName);
                ObjectMessage message = session.receiveObjectMessage();
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public Serializable receiveObjectMessageFromTopic(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName + " with timeout " + timeout + "ms");
                ObjectMessage message = session.receiveObjectMessage(timeout);
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public Serializable receiveObjectMessageFromTopicNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive object message from destination " + destinationName + " with no wait");
                ObjectMessage message = session.receiveObjectMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received object message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                Serializable retVal = message.getObject();
                message.acknowledge();
                return retVal;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive object message on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving object message", e);
        }
    }

    public String receiveTextMessageFromQueueIntoString(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName);
                TextMessage message = session.receiveTextMessage();
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public String receiveTextMessageFromQueueIntoString(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName + " with timeout " + timeout + "ms");
                TextMessage message = session.receiveTextMessage(timeout);
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public String receiveTextMessageFromQueueIntoStringNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching queue receive session for destination " + destinationName);
            QueueReceiveSession session = connection.getQueueReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName + " with no wait");
                TextMessage message = session.receiveTextMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No queue receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public String receiveTextMessageFromTopicIntoString(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName);
                TextMessage message = session.receiveTextMessage();
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public String receiveTextMessageFromTopicIntoString(String destinationName, long timeout) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName + " with timeout " + timeout + "ms");
                TextMessage message = session.receiveTextMessage(timeout);
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public String receiveTextMessageFromTopicIntoStringNoWait(String destinationName) throws OpenJMSAdapterException {
        try {
            logger.info("Fetching topic receive session for destination " + destinationName);
            TopicReceiveSession session = connection.getTopicReceiveSession(destinationName);
            if (session != null) {
                logger.info("Trying to receive text message from destination " + destinationName + " with no wait");
                TextMessage message = session.receiveTextMessageNoWait();
                if (message == null) {
                    logger.warn("Received null message");
                    return null;
                }
                logger.info("Received text message");
                logger.info("Extracted sequence number from message as " + message.getStringProperty("OpenJMSAdapterSequence"));
                manager.updateSequence(destinationName, Long.parseLong(message.getStringProperty("OpenJMSAdapterSequence")));
                String body = message.getText();
                message.acknowledge();
                return body;
            } else {
                logger.warn("No topic receive session found for destination " + destinationName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Encountered exception while trying to receive text message into a string on destination " + destinationName);
            throw new OpenJMSAdapterException("Error receiving text message as a string", e);
        }
    }

    public abstract void reconnectWith(long timeout) throws OpenJMSAdapterException;

    private long publishAsBytesMessage(String destinationName, OpenJMSAdapterPublisher session, String message) throws JMSException {
        logger.info("Fetched publish session for destination " + destinationName);
        BytesMessage bm = session.createBytesMessage();
        logger.info("Created BytesMessage on session, setting sequence number and message body");
        bm.writeBytes(message.getBytes());
        long temp = manager.getLastSequence(destinationName);
        temp++;
        bm.setLongProperty("OpenJMSAdapterSequence", temp);
        bm.setStringProperty("DestinationName", destinationName);
        session.publish(bm);
        manager.updateSequence(destinationName, temp);
        logger.info("Published message with sequence number " + temp);
        return temp;
    }

    private long publishAsTextMessage(String destinationName, OpenJMSAdapterPublisher session, String message) throws JMSException {
        logger.info("Fetched queue publish session for destination " + destinationName);
        TextMessage tm = session.createTextMessage();
        logger.info("Created TextMessage on session, setting sequence number and message body");
        tm.setText(message);
        long temp = manager.getLastSequence(destinationName);
        temp++;
        tm.setLongProperty("OpenJMSAdapterSequence", temp);
        tm.setStringProperty("DestinationName", destinationName);
        session.publish(tm);
        manager.updateSequence(destinationName, temp);
        logger.info("Published message with sequence number " + temp);
        return temp;
    }

    private long publishAsObjectMessage(String destinationName, OpenJMSAdapterPublisher session, Serializable message) throws JMSException {
        logger.info("Fetched publish session for destination " + destinationName);
        ObjectMessage om = session.createObjectMessage();
        logger.info("Created ObjectMessage on session, setting sequence number and message body");
        om.setObject(message);
        long temp = manager.getLastSequence(destinationName);
        temp++;
        om.setLongProperty("OpenJMSAdapterSequence", temp);
        om.setStringProperty("DestinationName", destinationName);
        session.publish(om);
        manager.updateSequence(destinationName, temp);
        logger.info("Published message with sequence number " + temp);
        return temp;
    }

    class SequenceManager {

        private HashMap<String, Long> sequences = new HashMap<String, Long>();

        void addDestination(String destination, long sequence) {
            sequences.put(destination, sequence);
        }

        long getLastSequence(String destination) {
            return sequences.get(destination);
        }

        void updateSequence(String destination, long sequence) {
            sequences.remove(destination);
            sequences.put(destination, sequence);
        }
    }
}
