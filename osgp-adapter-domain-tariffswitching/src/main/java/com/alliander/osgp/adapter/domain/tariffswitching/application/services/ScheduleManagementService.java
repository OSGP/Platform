/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.tariffswitching.application.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceOutputSetting;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.exceptions.ValidationException;
import com.alliander.osgp.domain.core.valueobjects.LightValue;
import com.alliander.osgp.domain.core.valueobjects.RelayType;
import com.alliander.osgp.domain.core.valueobjects.Schedule;
import com.alliander.osgp.dto.valueobjects.ScheduleMessageDataContainer;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;
import com.alliander.osgp.shared.infra.jms.RequestMessage;

@Service(value = "domainTariffSwitchingScheduleManagementService")
@Transactional(value = "transactionManager")
public class ScheduleManagementService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleManagementService.class);

    /**
     * Constructor
     */
    public ScheduleManagementService() {
        // Parameterless constructor required for transactions...
    }

    // === SET TARIFF SCHEDULE ===

    /**
     * Set a tariff schedule.
     *
     * @throws FunctionalException
     */
    public void setTariffSchedule(final String organisationIdentification, final String deviceIdentification,
            final String correlationUid, final List<Schedule> schedules, final Long scheduleTime,
            final String messageType) throws FunctionalException {

        LOGGER.info("setTariffSchedule called with organisation {} and device {}.", organisationIdentification,
                deviceIdentification);

        this.findOrganisation(organisationIdentification);
        final Device device = this.findActiveDevice(deviceIdentification);
        if (Ssld.PSLD_TYPE.equals(device.getDeviceType())) {
            throw new FunctionalException(FunctionalExceptionType.TARIFF_SCHEDULE_NOT_ALLOWED_FOR_PSLD,
                    ComponentType.DOMAIN_TARIFF_SWITCHING, new ValidationException(
                            "Set tariff schedule is not allowed for PSLD."));
        }

        // Reverse schedule switching for TARIFF_REVERSED relays.
        for (final DeviceOutputSetting dos : this.getSsldForDevice(device).getOutputSettings()) {
            if (dos.getOutputType().equals(RelayType.TARIFF_REVERSED)) {
                for (final Schedule schedule : schedules) {
                    for (final LightValue lightValue : schedule.getLightValue()) {
                        lightValue.invertIsOn();
                    }
                }
            }
        }

        LOGGER.info("Mapping to schedule DTO");

        final List<com.alliander.osgp.dto.valueobjects.Schedule> schedulesDto = this.domainCoreMapper.mapAsList(
                schedules, com.alliander.osgp.dto.valueobjects.Schedule.class);
        final ScheduleMessageDataContainer scheduleMessageDataContainerDto = new ScheduleMessageDataContainer(
                schedulesDto);

        LOGGER.info("Sending message");

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, scheduleMessageDataContainerDto), messageType, device.getIpAddress(),
                scheduleTime);
    }
}
