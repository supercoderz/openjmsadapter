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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

/**
 *
 * @author hari
 */
public interface OpenJMSAdapterReceiver extends OpenJMSAdapterSession{

    BytesMessage receiveBytesMessage() throws JMSException;

    BytesMessage receiveBytesMessage(long timeout)throws JMSException;

    BytesMessage receiveBytesMessageNoWait()throws JMSException;

    ObjectMessage receiveObjectMessage()throws JMSException;

    ObjectMessage receiveObjectMessage(long timeout)throws JMSException;

    ObjectMessage receiveObjectMessageNoWait()throws JMSException;

    TextMessage receiveTextMessage()throws JMSException;

    TextMessage receiveTextMessage(long timeout)throws JMSException;

    TextMessage receiveTextMessageNoWait()throws JMSException;

}
