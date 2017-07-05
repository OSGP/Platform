/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.OsgpCoreRequestMessageListener;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageListener;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceRequestMessageListener;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import com.alliander.osgp.shared.application.config.AbstractMessagingConfig;
import com.alliander.osgp.shared.application.config.jms.JmsConfiguration;
import com.alliander.osgp.shared.application.config.jms.JmsConfigurationFactory;
import com.alliander.osgp.shared.application.config.jms.JmsConfigurationNames;

/**
 * An application context Java configuration class.
 */
@Configuration
@PropertySources({ @PropertySource("classpath:osgp-adapter-domain-smartmetering.properties"),
    @PropertySource(value = "file:${osgp/Global/config}", ignoreResourceNotFound = true),
    @PropertySource(value = "file:${osgp/AdapterDomainSmartMetering/config}", ignoreResourceNotFound = true), })
public class MessagingConfig extends AbstractMessagingConfig {

    @Autowired
    @Qualifier("domainSmartMeteringIncomingWebServiceRequestMessageListener")
    private WebServiceRequestMessageListener incomingWebServiceRequestMessageListener;

    @Autowired
    @Qualifier("domainSmartMeteringIncomingOsgpCoreResponseMessageListener")
    private OsgpCoreResponseMessageListener incomingOsgpCoreResponseMessageListener;

    @Autowired
    @Qualifier("domainSmartMeteringIncomingOsgpCoreRequestMessageListener")
    private OsgpCoreRequestMessageListener incomingOsgpCoreRequestMessageListener;

    @Bean
    public JmsConfiguration incomingWebServiceJmsConfiguration(final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_INCOMING_WS_REQUESTS,
                this.incomingWebServiceRequestMessageListener);
    }

    @Bean(name = "domainSmartMeteringIncomingWebServiceRequestMessageListenerContainer")
    public DefaultMessageListenerContainer incomingWebServiceRequestsMessageListenerContainer(
            final JmsConfiguration incomingWebServiceJmsConfiguration) {
        return incomingWebServiceJmsConfiguration.getMessageListenerContainer();
    }

    // JMS SETTINGS: OUTGOING WEB SERVICE RESPONSES
    @Bean
    public JmsConfiguration outgoingWebServiceJmsConfiguration(final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_OUTGOING_WS_RESPONSES);
    }

    @Bean(name = "domainSmartMeteringOutgoingWebServiceResponsesJmsTemplate")
    public JmsTemplate outgoingWebServiceResponsesJmsTemplate(final JmsConfiguration outgoingWebServiceJmsConfiguration) {
        return outgoingWebServiceJmsConfiguration.getJmsTemplate();
    }

    @Bean(name = "domainSmartMeteringOutgoingWebServiceResponseMessageSender")
    public WebServiceResponseMessageSender outgoingWebServiceResponseMessageSender() {
        return new WebServiceResponseMessageSender();
    }

    // JMS SETTINGS: OUTGOING OSGP CORE REQUESTS (Sending requests to OSGP core)
    @Bean
    public JmsConfiguration outgoingOsgpCoreRequestsJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_OUTGOING_OSGP_CORE_REQUESTS);
    }

    @Bean(name = "domainSmartMeteringOutgoingOsgpCoreRequestsJmsTemplate")
    public JmsTemplate outgoingOsgpCoreRequestsJmsTemplate(
            final JmsConfiguration outgoingOsgpCoreRequestsJmsConfiguration) {
        return outgoingOsgpCoreRequestsJmsConfiguration.getJmsTemplate();
    }

    // JMS SETTINGS: INCOMING OSGP CORE RESPONSES (receiving responses from OSGP
    // core)

    @Bean
    public JmsConfiguration incomingOsgpCoreResponsesJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_INCOMING_OSGP_CORE_RESPONSES,
                this.incomingOsgpCoreResponseMessageListener);
    }

    @Bean(name = "domainSmartMeteringIncomingOsgpCoreResponsesMessageListenerContainer")
    public DefaultMessageListenerContainer incomingOsgpCoreResponsesMessageListenerContainer(
            final JmsConfiguration incomingOsgpCoreResponsesJmsConfiguration) {
        return incomingOsgpCoreResponsesJmsConfiguration.getMessageListenerContainer();
    }

    // JMS SETTINGS: INCOMING OSGP CORE REQUESTS (receiving requests from OSGP
    // core)

    @Bean
    public JmsConfiguration incomingOsgpCoreRequestsJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_INCOMING_OSGP_CORE_REQUESTS,
                this.incomingOsgpCoreRequestMessageListener);
    }

    @Bean(name = "domainSmartMeteringIncomingOsgpCoreRequestsMessageListenerContainer")
    public DefaultMessageListenerContainer incomingOsgpCoreRequestsMessageListenerContainer(
            final JmsConfiguration incomingOsgpCoreRequestsJmsConfiguration) {
        return incomingOsgpCoreRequestsJmsConfiguration.getMessageListenerContainer();
    }

    // JMS SETTINGS: OUTGOING OSGP CORE RESPONSES (sending responses to OSGP
    // core)

    @Bean
    public JmsConfiguration outgoingOsgpCoreResponsesJmsConfiguration(
            final JmsConfigurationFactory jmsConfigurationFactory) {
        return jmsConfigurationFactory.initializeConfiguration(JmsConfigurationNames.JMS_OUTGOING_OSGP_CORE_RESPONSES);
    }

    @Bean(name = "domainSmartMeteringOutgoingOsgpCoreResponsesJmsTemplate")
    public JmsTemplate outgoingOsgpCoreResponsesJmsTemplate(
            final JmsConfiguration outgoingOsgpCoreResponsesJmsConfiguration) {
        return outgoingOsgpCoreResponsesJmsConfiguration.getJmsTemplate();
    }
}
