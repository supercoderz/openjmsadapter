/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/configuration/DatabaseConfigHolderTest.java $
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
package com.openjmsadapter.configuration;

import com.openjmsadapter.configuration.database.DatabaseConfigHolder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class DatabaseConfigHolderTest {

    @Test
    public void testCreateDatabaseConfigurationHolder(){
        ConfigurationHolder holder = new DatabaseConfigHolder();
        assertNotNull(holder);
        assertNotNull(holder.getConfig());
    }

    // these two test are basically not needed - there are here only to init the DB
    
    @Test
    public void testCreateDatabaseConfigurationHolderWithCustomHibernateConfigFile(){
        ConfigurationHolder holder = new DatabaseConfigHolder("C:/NetBeansProjects/openjmsadapter/test/test_hibernate.cfg.xml");
        assertNotNull(holder);
        assertNotNull(holder.getConfig());
    }

    @Test
    public void testCreateDatabaseConfigurationHolderWithCustomHibernateConfigFile1(){
        ConfigurationHolder holder = new DatabaseConfigHolder("C:/NetBeansProjects/openjmsadapter/test/test_hibernate.cfg_1.xml");
        assertNotNull(holder);
        assertNotNull(holder.getConfig());
    }
    @Test
    public void testCreateDatabaseConfigurationHolderWithCustomHibernateConfigFile2(){
        assertNotNull(new DatabaseConfigHolder("C:/NetBeansProjects/openjmsadapter/test/test_hibernate.cfg_2.xml"));
    }

    @Test
    public void testCreateDatabaseConfigurationHolderWithCustomHibernateConfigFile3(){
        assertNotNull(new DatabaseConfigHolder("C:/NetBeansProjects/openjmsadapter/test/test_hibernate.cfg_3.xml"));
    }

}