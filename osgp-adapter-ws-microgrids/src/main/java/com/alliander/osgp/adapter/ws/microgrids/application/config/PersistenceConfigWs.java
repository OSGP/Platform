/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.microgrids.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.alliander.osgp.domain.core.exceptions.PlatformException;

@EnableJpaRepositories(entityManagerFactoryRef = "wsEntityManagerFactory", basePackageClasses = {
        com.alliander.osgp.adapter.ws.microgrids.domain.repositories.MeterResponseDataRepository.class })
@Configuration
@PropertySource("file:${osp/osgpAdapterWsMicrogrids/config}")
public class PersistenceConfigWs extends PersistenceConfigBase {

    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    public PersistenceConfigWs() {
        super(PROPERTY_NAME_DATABASE_USERNAME, PROPERTY_NAME_DATABASE_PASSWORD, PROPERTY_NAME_DATABASE_URL,
                PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN, PersistenceConfigWs.class);
    }

    @Bean
    public JpaTransactionManager wsTransactionManager() throws PlatformException {
        return this.createTransactionManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean wsEntityManagerFactory() throws ClassNotFoundException {
        return this.createEntityManagerFactory();
    }
}
