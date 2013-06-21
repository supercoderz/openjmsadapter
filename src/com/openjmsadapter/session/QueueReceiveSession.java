/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/session/QueueReceiveSession.java $
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

import com.openjmsadapter.transport.ReceiverEndpoint;
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
public class QueueReceiveSession implements OpenJMSAdapterReceiver {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final String destination;
    private final Session session;
    private boolean connected;
    private ReceiverEndpoint receiver;

    public QueueReceiveSession(String destination, Connection connection) throws JMSException {
        this.destination = destination;
        this.session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

    }

    public void connect() throws JMSException {
        if (!connected) {
            logger.debug("Connecting QueueReceiveSession to destination " + destination);
            receiver = new ReceiverEndpoint(session.createConsumer(session.createQueue(destination)));
            connected = true;
            logger.debug("Connected QueueReceiveSession to destination " + destination);
        } else {
            logger.debug("QueueReceiveSession already connected to destination " + destination);
        }
    }

    public void disconnect() throws JMSException {
        if (connected) {
            logger.debug("Disconnecting QueueReceiveSession");
            receiver.disconnect();
            session.close();
            connected = false;
            logger.debug("Disconnected QueueReceiveSession");
        } else {
            logger.debug("QueueReceiveSession is already disconnected");
        }
    }

    public BytesMessage receiveBytesMessage() throws JMSException{
        logger.debug("Receiving BytesMessage on QueueReceiveSession");
        BytesMessage msg = receiver.receiveBytesMessage();
        logger.debug("Received BytesMessage on QueueReceiveSession");
        return msg;
    }

    public BytesMessage receiveBytesMessage(long timeout) throws JMSException {
        logger.debug("Receiving BytesMessage on QueueReceiveSession with timeout "+timeout);
        BytesMessage msg = receiver.receiveBytesMessage(timeout);
        logger.debug("Received BytesMessage on QueueReceiveSession with timeout "+timeout);
        return msg;
    }

    public BytesMessage receiveBytesMessageNoWait() throws JMSException {
        logger.debug("Receiving BytesMessage on QueueReceiveSession with no wait");
        BytesMessage msg = receiver.receiveBytesMessageNoWait();
        logger.debug("Received BytesMessage on QueueReceiveSession with no wait");
        return msg;
    }

    public ObjectMessage receiveObjectMessage() throws JMSException {
        logger.debug("Receiving ObjectMessage on QueueReceiveSession");
        ObjectMessage msg = receiver.receiveObjectMessage();
        logger.debug("Received ObjectMessage on QueueReceiveSession");
        return msg;
    }

    public ObjectMessage receiveObjectMessage(long timeout) throws JMSException {
        logger.debug("Receiving ObjectMessage on QueueReceiveSession with timeout "+timeout);
        ObjectMessage msg = receiver.receiveObjectMessage(timeout);
        logger.debug("Received ObjectMessage on QueueReceiveSession with timeout "+timeout);
        return msg;
    }

    public ObjectMessage receiveObjectMessageNoWait() throws JMSException {
        logger.debug("Receiving ObjectMessage on QueueReceiveSession with no wait");
        ObjectMessage msg = receiver.receiveObjectMessageNoWait();
        logger.debug("Received ObjectMessage on QueueReceiveSession with no wait");
        return msg;
    }

    public TextMessage receiveTextMessage() throws JMSException {
        logger.debug("Receiving TextMessage on QueueReceiveSession");
        TextMessage msg = receiver.receiveTextMessage();
        logger.debug("Received TextMessage on QueueReceiveSession");
        return msg;
    }

    public TextMessage receiveTextMessage(long timeout) throws JMSException {
        logger.debug("Receiving TextMessage on QueueReceiveSession with timeout "+timeout);
        TextMessage msg = receiver.receiveTextMessage(timeout);
        logger.debug("Received TextMessage on QueueReceiveSession with timeout "+timeout);
        return msg;
    }

    public TextMessage receiveTextMessageNoWait() throws JMSException{
        logger.debug("Receiving TextMessage on QueueReceiveSession with no wait");
        TextMessage msg = receiver.receiveTextMessageNoWait();
        logger.debug("Received TextMessage on QueueReceiveSession with no wait");
        return msg;
    }
}
