/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/transport/ReceiverEndpoint.java $
 * $Revision: 21 $
 * $Date: 2010-01-02 13:35:09 +0000 (Sat, 02 Jan 2010) $
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
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class ReceiverEndpoint {
    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final MessageConsumer consumer;

    public ReceiverEndpoint(MessageConsumer consumer) {
        logger.debug("Initializing ReceiverEndPoint with consumer "+consumer);
        this.consumer=consumer;
        logger.debug("Completed initializing ReceiverEndPoint with consumer "+consumer);
    }

    public BytesMessage receiveBytesMessage() throws JMSException{
        logger.debug("Receiving BytesMessage in blocking mode on consumer "+consumer);
        BytesMessage msg =  (BytesMessage)consumer.receive();
        logger.debug("Completed receiving BytesMessage in blocking mode on consumer "+consumer);
        return msg;
    }

    public BytesMessage receiveBytesMessage(long timeout) throws JMSException{
        logger.debug("Receiving BytesMessage with "+timeout+"ms timeout on consumer "+consumer);
        BytesMessage msg =  (BytesMessage)consumer.receive(timeout);
        logger.debug("Completed receiving BytesMessage with "+timeout+"ms timeout on consumer "+consumer);
        return msg;
    }

    public BytesMessage receiveBytesMessageNoWait() throws JMSException{
        logger.debug("Receiving BytesMessage with no wait on consumer "+consumer);
        BytesMessage msg =  (BytesMessage)consumer.receiveNoWait();
        logger.debug("Completed receiving BytesMessage with no wait on consumer "+consumer);
        return msg;
    }

    public TextMessage receiveTextMessage() throws JMSException{
        logger.debug("Receiving TextMessage in blocking mode on consumer "+consumer);
        TextMessage msg = (TextMessage)consumer.receive();
        logger.debug("Completed receiving TextMessage in blocking mode on consumer "+consumer);
        return msg;
    }

    public TextMessage receiveTextMessage(long timeout) throws JMSException{
        logger.debug("Receiving TextMessage with "+timeout+"ms timeout on consumer "+consumer);
        TextMessage msg =  (TextMessage)consumer.receive(timeout);
        logger.debug("Completed receiving TextMessage with "+timeout+"ms timeout on consumer "+consumer);
        return msg;
    }

    public TextMessage receiveTextMessageNoWait() throws JMSException{
        logger.debug("Receiving TextMessage with no wait on consumer "+consumer);
        TextMessage msg =  (TextMessage)consumer.receiveNoWait();
        logger.debug("Completed receiving TextMessage with no wait on consumer "+consumer);
        return msg;
    }

    public ObjectMessage receiveObjectMessage() throws JMSException{
        logger.debug("Receiving ObjectMessage in blocking mode on consumer "+consumer);
        ObjectMessage msg = (ObjectMessage)consumer.receive();
        logger.debug("Completed receiving ObjectMessage in blocking mode on consumer "+consumer);
        return msg;
    }

    public ObjectMessage receiveObjectMessage(long timeout) throws JMSException{
        logger.debug("Receiving ObjectMessage with "+timeout+"ms timeout on consumer "+consumer);
        ObjectMessage msg =  (ObjectMessage)consumer.receive(timeout);
        logger.debug("Completed receiving ObjectMessage with "+timeout+"ms timeout on consumer "+consumer);
        return msg;
    }

    public ObjectMessage receiveObjectMessageNoWait() throws JMSException{
        logger.debug("Receiving ObjectMessage with no wait on consumer "+consumer);
        ObjectMessage msg =  (ObjectMessage)consumer.receiveNoWait();
        logger.debug("Completed receiving ObjectMessage with no wait on consumer "+consumer);
        return msg;
    }

    public void disconnect() throws JMSException {
        logger.debug("Disconnecting ReceiverEndpoint for consumer "+ consumer);
        consumer.close();
        logger.debug("Completed disconnecting ReceiverEndpoint for consumer "+ consumer);
    }


}
