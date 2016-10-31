/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.core.infra.jms.ws.messageprocessors;

import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.core.application.services.ConfigurationManagementService;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.data.JmsObjectMessageData;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;

/**
 * Class for processing common switch configuration request messages
 *
 */
@Component("domainCoreCommonSwitchConfigurationRequestMessageProcessor")
public class CommonSwitchConfigurationRequestMessageProcessor extends WebServiceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonSwitchConfigurationRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreConfigurationManagementService")
    private ConfigurationManagementService configurationManagementService;

    public CommonSwitchConfigurationRequestMessageProcessor() {
        super(DeviceFunction.SWITCH_CONFIGURATION_BANK);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing common switch configuration bank message");

        final JmsObjectMessageData messageData = this.getObjectMessageData(message);

        try {
            LOGGER.info("Calling application service function: {}", messageData.getMessageType());

            this.configurationManagementService.switchConfiguration(messageData.getOrganisationIdentification(),
                    messageData.getDeviceIdentification(), messageData.getCorrelationUid(),
                    messageData.getMessageType(), messageData.getResultObject());

        } catch (final Exception e) {
            this.handleError(e, messageData);
        }
    }
}
