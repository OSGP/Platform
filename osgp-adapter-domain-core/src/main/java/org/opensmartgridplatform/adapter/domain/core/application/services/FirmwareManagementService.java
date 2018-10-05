/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.core.application.services;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.validation.Identification;
import org.opensmartgridplatform.domain.core.valueobjects.FirmwareUpdateMessageDataContainer;
import org.opensmartgridplatform.dto.valueobjects.FirmwareVersionDto;
import org.opensmartgridplatform.shared.domain.CorrelationIds;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.exceptionhandling.TechnicalException;
import org.opensmartgridplatform.shared.infra.jms.RequestMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;

@Service(value = "domainCoreFirmwareManagementService")
@Transactional(value = "transactionManager")
public class FirmwareManagementService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagementService.class);

    /**
     * Constructor
     */
    public FirmwareManagementService() {
        // Parameterless constructor required for transactions...
    }

    // === UPDATE FIRMWARE ===

    public void updateFirmware(final CorrelationIds ids,
            final FirmwareUpdateMessageDataContainer firmwareUpdateMessageDataContainer, final Long scheduleTime,
            final String messageType, final int messagePriority) throws FunctionalException {

        LOGGER.debug("Update firmware called with organisation [{}], device [{}], firmwareIdentification [{}].",
                ids.getOrganisationIdentification(), ids.getDeviceIdentification(),
                firmwareUpdateMessageDataContainer.getFirmwareUrl());

        this.findOrganisation(ids.getOrganisationIdentification());
        final Device device = this.findActiveDevice(ids.getDeviceIdentification());

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(ids, this.domainCoreMapper.map(firmwareUpdateMessageDataContainer,
                                org.opensmartgridplatform.dto.valueobjects.FirmwareUpdateMessageDataContainer.class)),
                messageType, messagePriority, device.getIpAddress(), scheduleTime);
    }

    // === GET FIRMWARE VERSION ===

    public void getFirmwareVersion(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, final String correlationUid, final String messageType,
            final int messagePriority) throws FunctionalException {

        LOGGER.debug("Get firmware version called with organisation [{}], device [{}].", organisationIdentification,
                deviceIdentification);

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, null), messageType,
                messagePriority, device.getIpAddress());
    }

    public void handleGetFirmwareVersionResponse(final List<FirmwareVersionDto> firmwareVersions,
            final CorrelationIds ids, final String messageType, final int messagePriority,
            final ResponseMessageResultType deviceResult, final OsgpException exception) {

        LOGGER.info("handleResponse for MessageType: {}", messageType);

        ResponseMessageResultType result = ResponseMessageResultType.OK;
        OsgpException osgpException = exception;

        try {
            if (deviceResult == ResponseMessageResultType.NOT_OK || osgpException != null) {
                LOGGER.error("Device Response not ok.", osgpException);
                throw osgpException;
            }
        } catch (final Exception e) {
            LOGGER.error("Unexpected Exception", e);
            result = ResponseMessageResultType.NOT_OK;
            osgpException = new TechnicalException(ComponentType.UNKNOWN,
                    "Exception occurred while getting device firmware version", e);
        }

        final ResponseMessage responseMessage = ResponseMessage.newResponseMessageBuilder().withIds(ids)
                .withResult(result).withOsgpException(osgpException).withDataObject((Serializable) firmwareVersions)
                .withMessagePriority(messagePriority).build();
        this.webServiceResponseMessageSender.send(responseMessage);
    }

    // === SWITCH TO OTHER FIRMWARE VERSION ===

    public void switchFirmware(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final String messageType, final int messagePriority, final String version)
            throws FunctionalException {
        LOGGER.debug("switchFirmware called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, version),
                messageType, messagePriority, device.getIpAddress());
    }
}
