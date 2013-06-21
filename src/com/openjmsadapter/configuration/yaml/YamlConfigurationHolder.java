/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/configuration/yaml/YamlConfigurationHolder.java $
 * $Revision: 34 $
 * $Date: 2010-01-23 21:26:55 +0000 (Sat, 23 Jan 2010) $
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
package com.openjmsadapter.configuration.yaml;

import com.openjmsadapter.configuration.AdapterConfiguration;
import com.openjmsadapter.configuration.ConfigurationHolder;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.log4j.Logger;
import org.ho.yaml.Yaml;

/**
 *
 * @author hari
 */
public class YamlConfigurationHolder implements ConfigurationHolder {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private AdapterConfiguration config;
    private String filename;

    public YamlConfigurationHolder(String filename) throws FileNotFoundException {
        logger.debug("Creating YamlConfigurationHolder with configuration file "+filename + " and loading configuration");
        this.config = Yaml.loadType(new File(filename), com.openjmsadapter.configuration.AdapterConfiguration.class);
        this.filename = filename;
        logger.debug("Completed loading Yaml configuration");
    }

    public AdapterConfiguration getConfig() {
        return config;
    }

    public boolean persistConfiguration() throws Exception {
        logger.debug("Dumping Yaml configuration to file "+filename);
        Yaml.dump(config, new File(filename),true);
        logger.debug("Completed dumping Yaml configuration");
        return true;
    }

    public String getFilename() {
        return filename;
    }

}
