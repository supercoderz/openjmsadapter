/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/session/TopicPublishSession.java $
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
package com.openjmsadapter.session;

import com.openjmsadapter.transport.PublisherEndpoint;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class TopicPublishSession implements OpenJMSAdapterPublisher {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final String destination;
    private final Session session;
    private boolean connected = false;
    private PublisherEndpoint publisher;

    public TopicPublishSession(String destination, Connection connection) throws JMSException {
        this.destination = destination;
        this.session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
    }

    public void connect() throws JMSException {
        if (!connected) {
            logger.debug("Connecting TopicPublishSession to destination " + destination);
            publisher = new PublisherEndpoint(session.createProducer(session.createTopic(destination)));
            connected = true;
            logger.debug("Connected TopicPublishSession to destination " + destination);
        } else {
            logger.debug("TopicPublishSession already connected to destination " + destination);
        }
    }

    public void disconnect() throws JMSException {
        if (connected) {
            logger.debug("Disconnecting TopicPublishSession");
            publisher.disconnect();
            session.close();
            connected = false;
            logger.debug("Disconnected TopicPublishSession");
        } else {
            logger.debug("TopicPublishSession is already disconnected");
        }
    }

    public void publish(BytesMessage message) throws JMSException {
        logger.debug("Publishing BytesMessage from TopicPublishSession");
        publisher.sendBytesMessage(message);
        logger.debug("Published BytesMessage from TopicPublishSession");
    }

    public void publish(TextMessage message) throws JMSException {
        logger.debug("Publishing TextMessage from TopicPublishSession");
        publisher.sendTextMessage(message);
        logger.debug("Published TextMessage from TopicPublishSession");
    }

    public void publish(ObjectMessage message) throws JMSException {
        logger.debug("Publishing ObjectMessage from TopicPublishSession");
        publisher.sendObjectMessage(message);
        logger.debug("Published ObjectMessage from TopicPublishSession");
    }

    public BytesMessage createBytesMessage() throws JMSException {
        return session.createBytesMessage();
    }

    public TextMessage createTextMessage() throws JMSException {
        return session.createTextMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException {
        return session.createObjectMessage();
    }
}
