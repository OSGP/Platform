/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.tariffswitching.application.config;

import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.alliander.osgp.adapter.domain.tariffswitching.infra.jms.OsgpCoreRequestMessageListener;
import com.alliander.osgp.adapter.domain.tariffswitching.infra.jms.core.OsgpCoreResponseMessageListener;
import com.alliander.osgp.adapter.domain.tariffswitching.infra.jms.ws.WebServiceRequestMessageListener;
import com.alliander.osgp.adapter.domain.tariffswitching.infra.jms.ws.WebServiceResponseMessageSender;
import com.alliander.osgp.shared.application.config.AbstractMessagingConfig;
import com.alliander.osgp.shared.application.config.jms.JmsConfiguration;
import com.alliander.osgp.shared.application.config.jms.JmsConfigurationFactory;

/**
 * An application context Java configuration class.
 */
@Configuration
@PropertySources({ @PropertySource("classpath:osgp-adapter-domain-tariffswitching.properties"),
        @PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true),
        @PropertySource(value = "file:${osgp/AdapterDomainTariffSwitching/config}", ignoreResourceNotFound = true), })
public class MessagingConfig extends AbstractMessagingConfig {

    private static final String PROPERTY_NAME_JMS_DEFAULT_INITIAL_REDELIVERY_DELAY = "jms.default.initial.redelivery.delay";
    private static final String PROPERTY_NAME_JMS_DEFAULT_MAXIMUM_REDELIVERIES = "jms.default.maximum.redeliveries";
    private static final String PROPERTY_NAME_JMS_DEFAULT_MAXIMUM_REDELIVERY_DELAY = "jms.default.maximum.redelivery.delay";
    private static final String PROPERTY_NAME_JMS_DEFAULT_REDELIVERY_DELAY = "jms.default.redelivery.delay";

    @Autowired
    @Qualifier("domainTariffSwitchingIncomingWebServiceRequestMessageListener")
    private WebServiceRequestMessageListener incomingWebServiceRequestMessageListener;

    @Autowired
    @Qualifier("domainTariffSwitchingIncomingOsgpCoreResponseMessageListener")
    private OsgpCoreResponseMessageListener incomingOsgpCoreResponseMessageListener;

    // === JMS SETTINGS ===

    @Override
    @Bean
    public RedeliveryPolicy defaultRedeliveryPolicy() {
        final RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setInitialRedeliveryDelay(Long.parseLong(this.environment
                .getRequiredProperty(PROPERTY_NAME_JMS_DEFAULT_INITIAL_REDELIVERY_DELAY)));
        redeliveryPolicy.setMaximumRedeliveries(Integer.parseInt(this.environment
                .getRequiredProperty(PROPERTY_NAME_JMS_DEFAULT_MAXIMUM_REDELIVERIES)));
        redeliveryPolicy.setMaximumRedeliveryDelay(Long.parseLong(this.environment
                .getRequiredProperty(PROPERTY_NAME_JMS_DEFAULT_MAXIMUM_REDELIVERY_DELAY)));
        redeliveryPolicy.setRedeliveryDelay(Long.parseLong(this.environment
                .getRequiredProperty(PROPERTY_NAME_JMS_DEFAULT_REDELIVERY_DELAY)));

        return redeliveryPolicy;
    }

    // JMS SETTINGS: INCOMING WEB SERVICE REQUESTS ===

    @Bean
    public JmsConfiguration incomingWebServiceRequestsJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeReceiveConfiguration("jms.incoming.ws.requests",
                this.incomingWebServiceRequestMessageListener);
    }

    @Bean(name = "domainTariffSwitchingIncomingWebServiceRequestsMessageListenerContainer")
    public DefaultMessageListenerContainer incomingWebServiceRequestsMessageListenerContainer(
            final JmsConfiguration incomingWebServiceRequestsJmsConfiguration) {
        return incomingWebServiceRequestsJmsConfiguration.getMessageListenerContainer();
    }

    // JMS SETTINGS: OUTGOING WEB SERVICE RESPONSES

    @Bean
    public JmsConfiguration outgoingWebServiceResponsesJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration("jms.outgoing.ws.responses");
    }

    @Bean(name = "domainTariffSwitchingOutgoingWebServiceResponsesJmsTemplate")
    public JmsTemplate outgoingWebServiceResponsesJmsTemplate(
            final JmsConfiguration outgoingWebServiceResponsesJmsConfiguration) {
        return outgoingWebServiceResponsesJmsConfiguration.getJmsTemplate();
    }

    @Bean(name = "domainTariffSwitchingOutgoingWebServiceResponseMessageSender")
    public WebServiceResponseMessageSender outgoingWebServiceResponseMessageSender() {
        return new WebServiceResponseMessageSender();
    }

    // JMS SETTINGS: OUTGOING OSGP CORE REQUESTS (Sending requests to OSGP core)

    @Bean
    public JmsConfiguration outgoingOsgpCoreRequestsJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration("jms.outgoing.osgp.core.requests");
    }

    @Bean(name = "domainTariffSwitchingOutgoingOsgpCoreRequestsJmsTemplate")
    public JmsTemplate outgoingOsgpCoreRequestsJmsTemplate(
            final JmsConfiguration outgoingOsgpCoreRequestsJmsConfiguration) {
        return outgoingOsgpCoreRequestsJmsConfiguration.getJmsTemplate();
    }

    // JMS SETTINGS: INCOMING OSGP CORE RESPONSES (receiving responses from OSGP
    // core)

    @Bean
    public JmsConfiguration incomingOsgpCoreResponsesJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeReceiveConfiguration("jms.incoming.osgp.core.responses",
                this.incomingOsgpCoreResponseMessageListener);
    }

    @Bean(name = "domainTariffSwitchingIncomingOsgpCoreResponsesMessageListenerContainer")
    public DefaultMessageListenerContainer incomingOsgpCoreResponsesMessageListenerContainer(
            final JmsConfiguration incomingOsgpCoreResponsesJmsConfiguration) {
        return incomingOsgpCoreResponsesJmsConfiguration.getMessageListenerContainer();
    }

    // JMS SETTINGS: INCOMING OSGP CORE REQUESTS (receiving requests from OSGP
    // core)

    @Bean
    public JmsConfiguration incomingOsgpCoreRequestsJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeReceiveConfiguration("jms.incoming.osgp.core.requests",
                this.incomingWebServiceRequestMessageListener);
    }

    @Bean(name = "domainTariffSwitchingIncomingOsgpCoreRequestsMessageListenerContainer")
    public DefaultMessageListenerContainer incomingOsgpCoreRequestsMessageListenerContainer(
            final JmsConfiguration incomingOsgpCoreRequestsJmsConfiguration) {
        return incomingOsgpCoreRequestsJmsConfiguration.getMessageListenerContainer();
    }

    @Bean(name = "domainTariffSwitchingIncomingOsgpCoreRequestMessageListener")
    public OsgpCoreRequestMessageListener incomingOsgpCoreRequestMessageListener() {
        return new OsgpCoreRequestMessageListener();
    }

    // JMS SETTINGS: OUTGOING OSGP CORE RESPONSES (sending responses to OSGP
    // core)

    @Bean
    public JmsConfiguration outgoingOsgpCoreResponsesJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration("jms.outgoing.osgp.core.responses");
    }

    @Bean(name = "domainTariffSwitchingOutgoingOsgpCoreResponsesJmsTemplate")
    public JmsTemplate outgoingOsgpCoreResponsesJmsTemplate(
            final JmsConfiguration outgoingOsgpCoreResponsesJmsConfiguration) {
        return outgoingOsgpCoreResponsesJmsConfiguration.getJmsTemplate();
    }
}
