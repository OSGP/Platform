/**
 * Copyright 2015 Smart Society Services B.V.
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

import com.alliander.osgp.adapter.domain.core.application.services.FirmwareManagementService;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.data.JmsScheduledMessageData;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;

/**
 * Class for processing common update firmware request messages
 *
 * @author CGI
 *
 */
@Component("domainCoreCommonUpdateFirmwareRequestMessageProcessor")
public class CommonUpdateFirmwareRequestMessageProcessor extends WebServiceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpdateFirmwareRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreFirmwareManagementService")
    private FirmwareManagementService firmwareManagementService;

    public CommonUpdateFirmwareRequestMessageProcessor() {
        super(DeviceFunction.UPDATE_FIRMWARE);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing common update firmware request message");

        final JmsScheduledMessageData messageData = this.getScheduledMessageData(message);

        try {
            final String firmwareIdentification = (String) message.getObject();

            LOGGER.info("Calling application service function: {}", messageData.getMessageType());

            this.firmwareManagementService.updateFirmware(messageData.getOrganisationIdentification(),
                    messageData.getDeviceIdentification(), messageData.getCorrelationUid(), firmwareIdentification,
                    messageData.getScheduleTime(), messageData.getMessageType());

        } catch (final Exception e) {
            this.handleError(e, messageData);
        }
    }
}
