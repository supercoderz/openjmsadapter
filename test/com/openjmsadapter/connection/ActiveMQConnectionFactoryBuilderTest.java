/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/connection/ActiveMQConnectionFactoryBuilderTest.java $
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
package com.openjmsadapter.connection;

import com.openjmsadapter.configuration.yaml.YamlConfigurationHolder;
import com.openjmsadapter.exception.OpenJMSAdapterException;
import java.io.FileNotFoundException;
import javax.jms.ConnectionFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class ActiveMQConnectionFactoryBuilderTest {

    @Test
    public void testCreateActiveMQConnectionFactoryBuilder() throws FileNotFoundException{
        assertNotNull(new ActiveMQConnectionFactoryBuilder());
    }

    @Test
    public void testCreateConnectionFactory() throws FileNotFoundException, OpenJMSAdapterException{
        YamlConfigurationHolder holder = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        ActiveMQConnectionFactoryBuilder builder = new ActiveMQConnectionFactoryBuilder();
        assertNotNull(builder.getConnectionFactory(holder.getConfig()));
        assertTrue(builder.getConnectionFactory(holder.getConfig()) instanceof ConnectionFactory);
    }


}