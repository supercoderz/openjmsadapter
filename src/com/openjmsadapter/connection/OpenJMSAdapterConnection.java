/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/connection/OpenJMSAdapterConnection.java $
 * $Revision: 33 $
 * $Date: 2010-01-12 15:43:53 +0000 (Tue, 12 Jan 2010) $
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

import com.openjmsadapter.session.OpenJMSAdapterSession;
import com.openjmsadapter.session.QueueReceiveSession;
import com.openjmsadapter.session.TopicPublishSession;
import com.openjmsadapter.session.TopicReceiveSession;
import com.openjmsadapter.session.QueuePublishSession;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class OpenJMSAdapterConnection {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final ConnectionFactory connectionFactory;
    private final SessionBuilder manager = new SessionBuilder();
    private Connection connection;
    private boolean connected;
    private Map<String,OpenJMSAdapterSession> queuePublishSessions = new HashMap<String,OpenJMSAdapterSession>();
    private Map<String,OpenJMSAdapterSession> queueReceiveSessions = new HashMap<String,OpenJMSAdapterSession>();
    private Map<String,OpenJMSAdapterSession> topicPublishSessions = new HashMap<String,OpenJMSAdapterSession>();
    private Map<String,OpenJMSAdapterSession> topicReceiveSessions = new HashMap<String,OpenJMSAdapterSession>();

    public OpenJMSAdapterConnection(ConnectionFactory connectionFactory) {
        logger.debug("Initializing OpenJMSAdapterConnection with connection factory " + connectionFactory);
        this.connectionFactory = connectionFactory;
        logger.debug("Completed initializing OpenJMSAdapterConnection with connection factory " + connectionFactory);
    }

    public void connect() throws JMSException {
        if (!connected) {
            logger.debug("Connecting OpenJMSAdapterConnection to the JMS sever");
            this.connection = connectionFactory.createConnection();
            connection.start();
            this.connected = true;
            logger.debug("Connected OpenJMSAdapterConnection to the JMS sever");
        }
    }

    public void connect(String username,String password) throws JMSException {
        if (!connected) {
            logger.debug("Connecting OpenJMSAdapterConnection to the JMS sever");
            this.connection = connectionFactory.createConnection(username,password);
            connection.start();
            this.connected = true;
            logger.debug("Connected OpenJMSAdapterConnection to the JMS sever");
        }
    }

    public void disconnect() throws JMSException {
        logger.debug("Disconnecting OpenJMSAdapterConnection from the JMS sever");
        if (connected) {
            for(String key:queuePublishSessions.keySet()){
                queuePublishSessions.get(key).disconnect();
            }
            for(String key:queueReceiveSessions.keySet()){
                queueReceiveSessions.get(key).disconnect();
            }
            for(String key:topicPublishSessions.keySet()){
                topicPublishSessions.get(key).disconnect();
            }
            for(String key:topicReceiveSessions.keySet()){
                topicReceiveSessions.get(key).disconnect();
            }
            connection.close();
        }
        logger.debug("Disconnected OpenJMSAdapterConnection from the JMS sever");
    }

    public void createTopicPublishingSession(String destination) throws JMSException {
        if(!manager.sessionExists(destination)){
            logger.debug("Creating TopicPublishSession for destinaion "+destination);
            manager.addSession(destination, new TopicPublishSession(destination,connection));
        }
    }

    public void createQueuePublishingSession(String destination) throws JMSException {
        if(!manager.sessionExists(destination)){
            logger.debug("Creating TopicPublishSession for destinaion "+destination);
            manager.addSession(destination, new QueuePublishSession(destination,connection));
        }
    }

    public void createTopicReceivingSession(String destination) throws JMSException {
         if(!manager.sessionExists(destination)){
            logger.debug("Creating TopicReceiveSession for destinaion "+destination);
            manager.addSession(destination, new TopicReceiveSession(destination,connection));
        }
    }

    public void createQueueReceivingSession(String destination) throws JMSException {
        if(!manager.sessionExists(destination)){
            logger.debug("Creating QueueReceiveSession for destinaion "+destination);
            manager.addSession(destination, new QueueReceiveSession(destination,connection));
        }
    }

    public QueuePublishSession getQueuePublishSession(String key) {
        return (QueuePublishSession) queuePublishSessions.get(key);
    }

    public QueueReceiveSession getQueueReceiveSession(String key) {
        return (QueueReceiveSession) queueReceiveSessions.get(key);
    }

    public TopicPublishSession getTopicPublishSession(String key) {
        return (TopicPublishSession) topicPublishSessions.get(key);
    }

    public TopicReceiveSession getTopicReceiveSession(String key) {
        return (TopicReceiveSession) topicReceiveSessions.get(key);
    }

    class SessionBuilder {

        public boolean sessionExists(String destination) {
            return (topicPublishSessions.containsKey(destination)||
                    topicReceiveSessions.containsKey(destination)||
                    queuePublishSessions.containsKey(destination)||
                    queueReceiveSessions.containsKey(destination));
        }

        public void addSession(String destinantionName, TopicPublishSession session) throws JMSException {
            session.connect();
            topicPublishSessions.put(destinantionName, session);
        }

        public void addSession(String destinantionName, QueuePublishSession session) throws JMSException {
            session.connect();
            queuePublishSessions.put(destinantionName, session);
        }

        public void addSession(String destinantionName, TopicReceiveSession session) throws JMSException {
            session.connect();
            topicReceiveSessions.put(destinantionName, session);
        }

        public void addSession(String destinantionName, QueueReceiveSession session) throws JMSException {
            session.connect();
            queueReceiveSessions.put(destinantionName, session);
        }

    }
}
