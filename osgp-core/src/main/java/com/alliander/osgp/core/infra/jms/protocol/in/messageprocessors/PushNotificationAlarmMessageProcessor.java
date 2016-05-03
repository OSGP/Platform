/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.core.infra.jms.protocol.in.messageprocessors;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.core.application.services.EventNotificationMessageService;
import com.alliander.osgp.core.domain.model.domain.DomainRequestService;
import com.alliander.osgp.core.infra.jms.protocol.in.ProtocolRequestMessageProcessor;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceAuthorization;
import com.alliander.osgp.domain.core.entities.DomainInfo;
import com.alliander.osgp.domain.core.exceptions.UnknownEntityException;
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.DomainInfoRepository;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;
import com.alliander.osgp.dto.valueobjects.smartmetering.PushNotificationAlarmDto;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.infra.jms.Constants;
import com.alliander.osgp.shared.infra.jms.RequestMessage;

@Component("dlmsPushNotificationAlarmMessageProcessor")
@Transactional(value = "transactionManager")
public class PushNotificationAlarmMessageProcessor extends ProtocolRequestMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationAlarmMessageProcessor.class);

    @Autowired
    private EventNotificationMessageService eventNotificationMessageService;

    @Autowired
    private DomainRequestService domainRequestService;

    @Autowired
    private DomainInfoRepository domainInfoRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceAuthorizationRepository deviceAuthorizationRepository;

    protected PushNotificationAlarmMessageProcessor() {
        super(DeviceFunction.PUSH_NOTIFICATION_ALARM);
    }

    @Override
    public void processMessage(final ObjectMessage message) throws JMSException {
        final String messageType = message.getJMSType();
        final String organisationIdentification = message.getStringProperty(Constants.ORGANISATION_IDENTIFICATION);
        final String deviceIdentification = message.getStringProperty(Constants.DEVICE_IDENTIFICATION);

        LOGGER.info("Received message of messageType: {} organisationIdentification: {} deviceIdentification: {}",
                messageType, organisationIdentification, deviceIdentification);

        final RequestMessage requestMessage = (RequestMessage) message.getObject();
        final Object dataObject = requestMessage.getRequest();

        try {
            final PushNotificationAlarmDto pushNotificationAlarm = (PushNotificationAlarmDto) dataObject;

            this.storeAlarmAsEvent(pushNotificationAlarm);

            final String ownerIdentification = this.getOrganisationIdentificationOfOwner(deviceIdentification);
            LOGGER.info("Matching owner {} with device {} handling {} from {}", ownerIdentification,
                    deviceIdentification, messageType, requestMessage.getIpAddress());
            final RequestMessage requestWithUpdatedOrganization = new RequestMessage(
                    requestMessage.getCorrelationUid(), ownerIdentification, requestMessage.getDeviceIdentification(),
                    requestMessage.getIpAddress(), pushNotificationAlarm);

            /*
             * This message processor handles messages that came in on the
             * osgp-core.1_0.protocol-dlms.1_0.requests queue. Therefore lookup
             * the DomainInfo for DLMS (domain: SMART_METERING) version 1.0.
             * 
             * At some point in time there may be a cleaner solution, where the
             * DomainInfo can be derived from information in the message or JMS
             * metadata, but for now this will have to do.
             */
            final List<DomainInfo> domainInfos = this.domainInfoRepository.findAll();
            DomainInfo smartMeteringDomain = null;
            for (final DomainInfo di : domainInfos) {
                if ("SMART_METERING".equals(di.getDomain()) && "1.0".equals(di.getDomainVersion())) {
                    smartMeteringDomain = di;
                    break;
                }
            }

            if (smartMeteringDomain == null) {
                LOGGER.error(
                        "No DomainInfo found for SMART_METERING 1.0, unable to send message of message type: {} to domain adapter. RequestMessage for {} dropped.",
                        messageType, pushNotificationAlarm);
            } else {
                this.domainRequestService.send(requestWithUpdatedOrganization,
                        DeviceFunction.PUSH_NOTIFICATION_ALARM.name(), smartMeteringDomain);
            }

        } catch (final Exception e) {
            LOGGER.error("Exception", e);
            throw new JMSException(e.getMessage());
        }
    }

    private void storeAlarmAsEvent(final PushNotificationAlarmDto pushNotificationAlarm) {
        try {
            /*
             * Push notifications for alarms don't contain date/time info, use
             * new Date() as time with the notification.
             */
            this.eventNotificationMessageService.handleEvent(pushNotificationAlarm.getDeviceIdentification(),
                    new Date(), com.alliander.osgp.domain.core.valueobjects.EventType.ALARM_NOTIFICATION,
                    pushNotificationAlarm.getAlarms().toString(), 0);
        } catch (final UnknownEntityException uee) {
            LOGGER.warn("Unable to store event for Push Notification Alarm from unknown device: {}",
                    pushNotificationAlarm, uee);
        } catch (final Exception e) {
            LOGGER.error("Error storing event for Push Notification Alarm: {}", pushNotificationAlarm, e);
        }
    }

    private String getOrganisationIdentificationOfOwner(final String deviceIdentification) throws OsgpException {

        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);

        if (device == null) {
            LOGGER.error("No known device for deviceIdentification {} with alarm notification", deviceIdentification);
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_DEVICE, ComponentType.OSGP_CORE,
                    new UnknownEntityException(Device.class, deviceIdentification));
        }

        final List<DeviceAuthorization> deviceAuthorizations = this.deviceAuthorizationRepository
                .findByDeviceAndFunctionGroup(device, DeviceFunctionGroup.OWNER);

        if (deviceAuthorizations == null || deviceAuthorizations.isEmpty()) {
            LOGGER.error("No owner authorization for deviceIdentification {} with alarm notification",
                    deviceIdentification);
            throw new FunctionalException(FunctionalExceptionType.UNAUTHORIZED, ComponentType.OSGP_CORE,
                    new UnknownEntityException(DeviceAuthorization.class, deviceIdentification));
        }

        return deviceAuthorizations.get(0).getOrganisation().getOrganisationIdentification();
    }
}
