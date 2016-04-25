/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.controllableload.infra.jms.ws.messageprocessors;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.controllableload.application.services.AdHocManagementService;
import com.alliander.osgp.adapter.domain.controllableload.infra.jms.ws.WebServiceRequestMessageProcessor;
import com.alliander.osgp.domain.controllableload.valueobjects.RelayValue;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.shared.infra.jms.Constants;

/**
 * Class for processing controllable load switch device request messages
 */
@Component("domainControllableLoadSwitchDeviceRequestMessageProcessor")
public class SwitchDeviceRequestMessageProcessor extends WebServiceRequestMessageProcessor {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchDeviceRequestMessageProcessor.class);

    @Autowired
    @Qualifier("domainControllableLoadAdHocManagementService")
    private AdHocManagementService adHocManagementService;

    public SwitchDeviceRequestMessageProcessor() {
        super(DeviceFunction.SWITCH_DEVICE);
    }

    @Override
    public void processMessage(final ObjectMessage message) {
        LOGGER.debug("Processing controllable load switch device request message");

        String correlationUid = null;
        String messageType = null;
        String organisationIdentification = null;
        String deviceIdentification = null;
        Object dataObject = null;

        try {
            correlationUid = message.getJMSCorrelationID();
            messageType = message.getJMSType();
            organisationIdentification = message.getStringProperty(Constants.ORGANISATION_IDENTIFICATION);
            deviceIdentification = message.getStringProperty(Constants.DEVICE_IDENTIFICATION);
            dataObject = message.getObject();
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

            final List<RelayValue> relayValues = this.parse(dataObject);

            this.adHocManagementService.switchDevice(organisationIdentification, deviceIdentification, correlationUid,
                    relayValues, messageType);

        } catch (final Exception e) {
            this.handleError(e, correlationUid, organisationIdentification, deviceIdentification, messageType);
        }
    }

    private List<RelayValue> parse(final Object object) {
        final List<RelayValue> relayValues = new ArrayList<RelayValue>();
        if (object instanceof ArrayList<?>) {
            final ArrayList<?> al = (ArrayList<?>) object;
            for (final Object o : al) {
                if (o instanceof RelayValue) {
                    relayValues.add((RelayValue) o);
                }
            }
        }
        return relayValues;

    }
}
