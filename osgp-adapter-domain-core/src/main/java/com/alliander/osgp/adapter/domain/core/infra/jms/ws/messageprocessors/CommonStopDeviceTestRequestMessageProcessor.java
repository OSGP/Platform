/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.core.infra.jms.ws.messageprocessors;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.core.application.services.DeviceInstallationService;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.data.JmsMessageData;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;

/**
 * Class for processing common stop device test request message
 *
 * @author CGI
 *
 */
@Component("domainCoreCommonStopDeviceTestRequestMessageProcessor")
public class CommonStopDeviceTestRequestMessageProcessor extends WebServiceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonStopDeviceTestRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreDeviceInstallationService")
    private DeviceInstallationService deviceInstallationService;

    public CommonStopDeviceTestRequestMessageProcessor() {
        super(DeviceFunction.STOP_SELF_TEST);
    }

    @Override
    public void processMessage(final ObjectMessage message) throws JMSException {
        LOGGER.debug("Processing common stop device test request message");

        final JmsMessageData messageData = this.getMessageData(message);

        try {
            LOGGER.info("Calling application service function: {}", messageData.getMessageType());

            this.deviceInstallationService.stopSelfTest(messageData.getOrganisationIdentification(),
                    messageData.getDeviceIdentification(), messageData.getCorrelationUid(),
                    messageData.getMessageType());

        } catch (final Exception e) {
            this.handleError(e, messageData);
        }
    }
}
