/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/configuration/AdapterConfiguration.java $
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
package com.openjmsadapter.configuration;

import java.util.List;

/**
 *
 * @author hari
 */
public class AdapterConfiguration {

    private String JMSConnectionUrl;
    private String JMSConnectionUser;
    private String JMSConnectionPassword;
    private DestinationConfiguration[] destinations;

    public AdapterConfiguration() {
    }

    public String getJMSConnectionPassword() {
        return JMSConnectionPassword;
    }

    public void setJMSConnectionPassword(String JMSConnectionPassword) {
        this.JMSConnectionPassword = JMSConnectionPassword;
    }

    public String getJMSConnectionUrl() {
        return JMSConnectionUrl;
    }

    public void setJMSConnectionUrl(String JMSConnectionUrl) {
        this.JMSConnectionUrl = JMSConnectionUrl;
    }

    public String getJMSConnectionUser() {
        return JMSConnectionUser;
    }

    public void setJMSConnectionUser(String JMSConnectionUser) {
        this.JMSConnectionUser = JMSConnectionUser;
    }

    public DestinationConfiguration[] getDestinations() {
        return destinations;
    }

    public void setDestinations(DestinationConfiguration[] destinations) {
        this.destinations = destinations;
    }
}

