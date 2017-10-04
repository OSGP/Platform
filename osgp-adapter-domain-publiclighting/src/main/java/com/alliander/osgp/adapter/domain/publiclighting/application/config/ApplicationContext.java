/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.publiclighting.application.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * An application context Java configuration class. The usage of Java
 * configuration requires Spring Framework 3.0
 */
@Configuration
@ComponentScan(basePackages = { "com.alliander.osgp.domain.core", "com.alliander.osgp.adapter.domain.publiclighting" })
@EnableTransactionManagement
public class ApplicationContext {

    @Resource
    private Environment environment;

    private static final String PROPERTY_NAME_SET_TRANSITION_LOGS_RESPONSE = "public.lighting.set.transition.logs.response";

    @Bean
    public Boolean isSetTransitionResponseLoggingEnabled() {
        return Boolean.parseBoolean(this.environment.getRequiredProperty(PROPERTY_NAME_SET_TRANSITION_LOGS_RESPONSE));
    }

}
