/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.exceptions.InactiveDeviceException;
import com.alliander.osgp.domain.core.exceptions.UnknownEntityException;
import com.alliander.osgp.domain.core.exceptions.UnregisteredDeviceException;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.SsldRepository;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.domain.core.valueobjects.DeviceLifecycleStatus;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;

@Service
@Validated
@Transactional(value = "transactionManager")
public class DeviceDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDomainService.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SsldRepository ssldRepository;

    public Device saveDevice(final Device device) {
        return this.deviceRepository.save(device);
    }

    public Device searchDevice(@Identification final String deviceIdentification) throws FunctionalException {

        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        if (device == null) {
            LOGGER.error("Device with id {} could not be found", deviceIdentification);
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_DEVICE, ComponentType.DOMAIN_CORE,
                    new UnknownEntityException(Device.class, deviceIdentification));
        }
        return device;
    }

    public Device searchActiveDevice(@Identification final String deviceIdentification, final ComponentType osgpComponent)
            throws FunctionalException {

        final Device device = this.searchDevice(deviceIdentification);
        final Ssld ssld = this.ssldRepository.findOne(device.getId());

        if (!device.isActivated() || !device.getDeviceLifecycleStatus().equals(DeviceLifecycleStatus.IN_USE)) {
            throw new FunctionalException(FunctionalExceptionType.INACTIVE_DEVICE, osgpComponent,
                    new InactiveDeviceException(deviceIdentification));
        }

        // Note: since this code is still specific for SSLD / PSLD, this null
        // check is needed.
        if (ssld != null && !ssld.isPublicKeyPresent()) {
            throw new FunctionalException(FunctionalExceptionType.UNREGISTERED_DEVICE, osgpComponent,
                    new UnregisteredDeviceException(deviceIdentification));
        }

        return device;
    }
}
