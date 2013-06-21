/*
 * $HeadURL: file:///nfs/sf-svn/o/op/openjmsadapter/code/openjmsadapter/src/com/openjmsadapter/configuration/database/DatabaseConfigHolder.java $
 * $Revision: 35 $
 * $Date: 2010-01-23 21:30:56 +0000 (Sat, 23 Jan 2010) $
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
package com.openjmsadapter.configuration.database;

import com.openjmsadapter.configuration.AdapterConfiguration;
import com.openjmsadapter.configuration.ConfigurationHolder;
import com.openjmsadapter.configuration.DestinationConfiguration;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author hari
 */
public class DatabaseConfigHolder implements ConfigurationHolder {

    private static Logger logger = Logger.getLogger("OpenJMSAdapter");
    private AdapterConfiguration config = null;
    private final SessionFactory sessionFactory;

    public DatabaseConfigHolder() {
        logger.debug("Creating hibernate session factory");
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        buildConfig();
        logger.debug("Created hibernate session factory");
    }

    public DatabaseConfigHolder(String filename) {
        logger.debug("Creating hibernate session factory");
        this.sessionFactory = new Configuration().configure(new File(filename)).buildSessionFactory();
        buildConfig();
        logger.debug("Created hibernate session factory");
    }

    public AdapterConfiguration getConfig() {
        return config;
    }

    public String getFilename() {
        return null;
    }

    public boolean persistConfiguration() throws Exception {
        sessionFactory.getCurrentSession().beginTransaction();
        for(DestinationConfiguration dest_config:config.getDestinations()){
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(JmsDestinationParameters.class);
            criteria.setLockMode(LockMode.UPGRADE);
            criteria.add(Restrictions.like("destinationName", dest_config.getDestinantionName()));
            JmsDestinationParameters param = (JmsDestinationParameters) criteria.uniqueResult();
            param.setLastSequenceReceived(dest_config.getLastSequenceReceived());
            param.setLastSequenceSent(dest_config.getLastSequenceSent());
            sessionFactory.getCurrentSession().save(param);
        }
        sessionFactory.getCurrentSession().getTransaction().commit();
        sessionFactory.getCurrentSession().close();
        sessionFactory.close();
        return true;
    }

    private void buildConfig() {
        sessionFactory.getCurrentSession().beginTransaction();
        List<JmsConnectionParameters> conn_params_list = sessionFactory.getCurrentSession().createCriteria(JmsConnectionParameters.class).list();
        JmsConnectionParameters conn_param = conn_params_list.get(0);
        config=new AdapterConfiguration();
        config.setJMSConnectionUrl(conn_param.getUrl());
        config.setJMSConnectionUser(conn_param.getUsername());
        config.setJMSConnectionPassword(conn_param.getPassword());
        List<JmsDestinationParameters> dest_params_list = sessionFactory.getCurrentSession().createCriteria(JmsDestinationParameters.class).list();
        DestinationConfiguration[] destinations = new DestinationConfiguration[dest_params_list.size()];
        int i=0;
        for(JmsDestinationParameters dest_param:dest_params_list){
            DestinationConfiguration dest_config = new DestinationConfiguration();
            dest_config.setDestinantionName(dest_param.getDestinationName());
            dest_config.setDestinationType(dest_param.getDestinationType());
            dest_config.setOperationType(dest_param.getOperationType());
            dest_config.setLastSequenceReceived(dest_param.getLastSequenceReceived());
            dest_config.setLastSequenceSent(dest_param.getLastSequenceSent());
            destinations[i++]=dest_config;
        }
        config.setDestinations(destinations);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }
}
