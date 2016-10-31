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

import com.alliander.osgp.adapter.domain.core.application.services.DeviceManagementService;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.adapter.domain.core.infra.jms.ws.data.JmsMessageData;
import com.alliander.osgp.domain.core.valueobjects.Certification;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;

/**
 * Class for processing common update device ssl certification request messages
 *
 */
@Component("domainCoreCommonUpdateDeviceSslCertificationRequestMessageProcessor")
public class CommonUpdateDeviceSslCertificationRequestMessageProcessor extends WebServiceRequestMessageProcessor {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonUpdateDeviceSslCertificationRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreDeviceManagementService")
    private DeviceManagementService deviceManagementService;

    public CommonUpdateDeviceSslCertificationRequestMessageProcessor() {
        super(DeviceFunction.UPDATE_DEVICE_SSL_CERTIFICATION);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing update device ssl certification message");

        final JmsMessageData messageData = this.getMessageData(message);

        try {
            final Certification certification = (Certification) message.getObject();

            LOGGER.info("Calling application service function: {}", messageData.getMessageType());

            this.deviceManagementService.updateDeviceSslCertification(messageData.getOrganisationIdentification(),
                    messageData.getDeviceIdentification(), messageData.getCorrelationUid(), certification,
                    messageData.getMessageType());

        } catch (final Exception e) {
            this.handleError(e, messageData);
        }
    }
}
