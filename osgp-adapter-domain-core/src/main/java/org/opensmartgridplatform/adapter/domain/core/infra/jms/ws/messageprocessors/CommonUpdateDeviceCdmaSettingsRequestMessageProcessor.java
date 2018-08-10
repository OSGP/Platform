/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.core.infra.jms.ws.messageprocessors;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.opensmartgridplatform.adapter.domain.core.application.services.DeviceManagementService;
import org.opensmartgridplatform.adapter.domain.core.infra.jms.ws.WebServiceRequestMessageProcessor;
import org.opensmartgridplatform.domain.core.valueobjects.CdmaSettings;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.shared.infra.jms.Constants;
import org.opensmartgridplatform.shared.wsheaderattribute.priority.MessagePriorityEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class for processing common set device verification key request messages
 */
@Component("domainCoreCommonUpdateDeviceCdmaSettingsRequestMessageProcessor")
public class CommonUpdateDeviceCdmaSettingsRequestMessageProcessor extends WebServiceRequestMessageProcessor {

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonUpdateDeviceCdmaSettingsRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainCoreDeviceManagementService")
    private DeviceManagementService deviceManagementService;

    public CommonUpdateDeviceCdmaSettingsRequestMessageProcessor() {
        super(DeviceFunction.UPDATE_DEVICE_CDMA_SETTINGS);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing common set device verification key message");

        String correlationUid = null;
        String messageType = null;
        String organisationIdentification = null;
        String deviceIdentification = null;
        CdmaSettings cdmaSettings = null;

        try {
            correlationUid = message.getJMSCorrelationID();
            messageType = message.getJMSType();
            organisationIdentification = message.getStringProperty(Constants.ORGANISATION_IDENTIFICATION);
            deviceIdentification = message.getStringProperty(Constants.DEVICE_IDENTIFICATION);
            cdmaSettings = (CdmaSettings) message.getObject();
        } catch (final JMSException e) {
            LOGGER.error("UNRECOVERABLE ERROR, unable to read ObjectMessage instance, giving up.", e);
            LOGGER.debug("correlationUid: {}", correlationUid);
            LOGGER.debug("messageType: {}", messageType);
            LOGGER.debug("organisationIdentification: {}", organisationIdentification);
            LOGGER.debug("deviceIdentification: {}", deviceIdentification);
            return;
        }

        try {
            LOGGER.info("Calling application service function: {}", messageType);

            this.deviceManagementService.updateDeviceCdmaSettings(organisationIdentification, deviceIdentification,
                    correlationUid, cdmaSettings);
        } catch (final Exception e) {
            this.handleError(e, correlationUid, organisationIdentification, deviceIdentification, messageType,
                    MessagePriorityEnum.DEFAULT.getPriority());
        }
    }
}
