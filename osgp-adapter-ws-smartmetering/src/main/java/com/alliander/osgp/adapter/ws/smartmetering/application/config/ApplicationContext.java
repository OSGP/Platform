/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.alliander.osgp.adapter.ws.infra.specifications.JpaEventSpecifications;
import com.alliander.osgp.domain.core.specifications.EventSpecifications;

/**
 * An application context Java configuration class. The usage of Java
 * configuration requires Spring Framework 3.0
 */
@Configuration
@ComponentScan(basePackages = { "com.alliander.osgp.domain.core", "com.alliander.osgp.adapter.ws.smartmetering" })
@EnableTransactionManagement()
@ImportResource("classpath:applicationContext.xml")
@Import({ PersistenceConfig.class, CorePersistenceConfig.class, MessagingConfig.class, WebServiceConfig.class })
@PropertySource("file:${osp/osgpAdapterWsSmartMetering/config}")
public class ApplicationContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);

    /**
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LOGGER.debug("Initializing Local Validator Factory Bean");
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        final org.springframework.core.io.Resource[] resources = { new ClassPathResource("constraint-mappings.xml") };
        localValidatorFactoryBean.setMappingLocations(resources);
        return localValidatorFactoryBean;
    }

    /**
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        LOGGER.debug("Initializing Method Validation Post Processor Bean");

        final MethodValidationPostProcessor m = new MethodValidationPostProcessor();
        m.setValidatorFactory(this.validator());
        return m;
    }

    @Bean
    public EventSpecifications eventSpecifications() {
        return new JpaEventSpecifications();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        final PropertySourcesPlaceholderConfigurer propertySource = new PropertySourcesPlaceholderConfigurer();
        propertySource.setIgnoreUnresolvablePlaceholders(true);
        return propertySource;
    }

}
