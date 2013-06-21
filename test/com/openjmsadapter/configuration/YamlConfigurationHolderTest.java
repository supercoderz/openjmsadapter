/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/test/com/openjmsadapter/configuration/YamlConfigurationHolderTest.java $
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

import com.openjmsadapter.configuration.yaml.YamlConfigurationHolder;
import java.io.FileNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hari
 */
public class YamlConfigurationHolderTest {

    @Test
    public void testInitializeYamlConfiguration() throws FileNotFoundException {
        YamlConfigurationHolder config = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        assertNotNull(config);
        assertNotNull(config.getConfig());
        assertNotNull(config.getConfig().getDestinations());
        assertNotNull(config.getConfig().getJMSConnectionPassword());
        assertNotNull(config.getConfig().getJMSConnectionUser());
        assertNotNull(config.getConfig().getJMSConnectionUrl());
    }

    @Test
    public void testPersistToFile() throws Exception {
        YamlConfigurationHolder config = new YamlConfigurationHolder("C:/NetBeansProjects/openjmsadapter/test/yaml_config.yml");
        assertNotNull(config);
        config.persistConfiguration();
    }
}
