/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/transport/PublisherEndpoint.java $
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
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import org.apache.log4j.Logger;

/**
 *
 * @author hari
 */
public class PublisherEndpoint {
    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private final MessageProducer producer;

    public PublisherEndpoint(MessageProducer producer) {
        logger.debug("Initializing PublisherEndPoint with producer "+producer);
        this.producer = producer;
        logger.debug("Completed initializing PublisherEndPoint with producer "+producer);
    }

    public void sendBytesMessage(BytesMessage message) throws JMSException{
        logger.debug("Publishing BytesMessage on producer "+producer);
        producer.send(message);
        logger.debug("Finished publishing BytesMessage on producer "+producer);
    }

    public void sendTextMessage(TextMessage message) throws JMSException{
        logger.debug("Publishing TextMessage on producer "+producer);
        producer.send(message);
        logger.debug("Finished publishing TextMessage on producer "+producer);
    }

    public void sendObjectMessage(ObjectMessage message) throws JMSException{
        logger.debug("Publishing ObjectMessage on producer "+producer);
        producer.send(message);
        logger.debug("Finished publishing ObjectMessage on producer "+producer);
    }

    public void disconnect() throws JMSException{
        logger.debug("Disconnecting PublisherEndPoint for producer "+ producer);
        producer.close();
        logger.debug("Completed disconnecting PublisherEndPoint for producer "+ producer);
    }
}
