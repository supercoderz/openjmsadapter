/*
 * $HeadURL$
 * $Revision$
 * $Date$
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
package com.openjmsadapter.testsuite;

import com.openjmsadapter.client.database.DatabaseActiveMQClientPerfTest;
import com.openjmsadapter.client.database.DatabaseOpenMQClientPerfTest;
import com.openjmsadapter.client.yaml.YamlActiveMQClientPerfTest;
import com.openjmsadapter.client.yaml.YamlOpenMQClientPerfTest;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author hari
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    YamlActiveMQClientPerfTest.class,
    YamlOpenMQClientPerfTest.class,
    DatabaseActiveMQClientPerfTest.class,
    DatabaseOpenMQClientPerfTest.class
})

public class OpenJMSAdapterBasicPerfTest {

    @BeforeClass
    public static void loadLog4J() {
        PropertyConfigurator.configure("log4j.properties");
    }
}