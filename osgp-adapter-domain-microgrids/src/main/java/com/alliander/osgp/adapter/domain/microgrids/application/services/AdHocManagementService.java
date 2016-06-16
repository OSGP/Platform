/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.microgrids.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.microgrids.valueobjects.DataRequest;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.infra.jms.RequestMessage;

@Service(value = "domainMicrogridsAdHocManagementService")
@Transactional(value = "transactionManager")
public class AdHocManagementService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdHocManagementService.class);

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Constructor
     */
    public AdHocManagementService() {
        // Parameterless constructor required for transactions...
    }

    // === GET DATA ===

    /**
     * Retrieve data from of device
     *
     * @param organisationIdentification
     *            identification of organisation
     * @param deviceIdentification
     *            identification of device
     *
     * @return status of device
     *
     * @throws FunctionalException
     */
    public void getData(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final String messageType, final DataRequest dataRequest)
            throws FunctionalException {

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);

        this.osgpCoreRequestMessageSender.send(
                new RequestMessage(correlationUid, organisationIdentification, deviceIdentification, dataRequest),
                messageType, device.getIpAddress());
    }

    // public void handleGetDataResponse(final
    // com.alliander.osgp.dto.valueobjects.DeviceStatusDto deviceStatusDto,
    // final DomainType allowedDomainType, final String deviceIdentification,
    // final String organisationIdentification, final String correlationUid,
    // final String messageType,
    // final ResponseMessageResultType deviceResult, final OsgpException
    // exception) {
    //
    // LOGGER.info("handleResponse for MessageType: {}", messageType);
    //
    // ResponseMessageResultType result = ResponseMessageResultType.OK;
    // OsgpException osgpException = exception;
    // DeviceStatusMapped deviceStatusMapped = null;
    //
    // try {
    // if (deviceResult == ResponseMessageResultType.NOT_OK || osgpException !=
    // null) {
    // LOGGER.error("Device Response not ok.", osgpException);
    // throw osgpException;
    // }
    //
    // final DeviceStatus status = this.domainCoreMapper.map(deviceStatusDto,
    // DeviceStatus.class);
    //
    // final Ssld device =
    // this.RtuDeviceRepository.findByDeviceIdentification(deviceIdentification);
    //
    // final List<DeviceOutputSetting> deviceOutputSettings =
    // device.getOutputSettings();
    //
    // final Map<Integer, DeviceOutputSetting> dosMap = new HashMap<>();
    // for (final DeviceOutputSetting dos : deviceOutputSettings) {
    // dosMap.put(dos.getExternalId(), dos);
    // }
    //
    // deviceStatusMapped = new
    // DeviceStatusMapped(filterTariffValues(status.getLightValues(), dosMap,
    // allowedDomainType), filterLightValues(status.getLightValues(), dosMap,
    // allowedDomainType),
    // status.getPreferredLinkType(), status.getActualLinkType(),
    // status.getLightType(),
    // status.getEventNotificationsMask());
    //
    // } catch (final Exception e) {
    // LOGGER.error("Unexpected Exception", e);
    // result = ResponseMessageResultType.NOT_OK;
    // osgpException = new TechnicalException(ComponentType.UNKNOWN,
    // "Exception occurred while getting device status", e);
    // }
    //
    // this.webServiceResponseMessageSender.send(new
    // ResponseMessage(correlationUid, organisationIdentification,
    // deviceIdentification, result, osgpException, deviceStatusMapped));
    // }

}
