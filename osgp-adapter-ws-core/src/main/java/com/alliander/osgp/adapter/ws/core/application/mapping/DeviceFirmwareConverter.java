/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.core.application.mapping;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.DeviceFirmware;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.Firmware;
import com.alliander.osgp.adapter.ws.shared.db.domain.repositories.writable.WritableFirmwareFileRepository;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceFirmwareFile;
import com.alliander.osgp.domain.core.entities.FirmwareFile;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

class DeviceFirmwareConverter extends
        BidirectionalConverter<com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.DeviceFirmware, DeviceFirmwareFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFirmwareConverter.class);

    private DeviceRepository deviceRepository;
    private WritableFirmwareFileRepository firmwareFileRepository;

    public DeviceFirmwareConverter(final DeviceRepository deviceRepository,
            final WritableFirmwareFileRepository firmwareFileRepository) {
        this.deviceRepository = deviceRepository;
        this.firmwareFileRepository = firmwareFileRepository;
    }

    @Override
    public DeviceFirmwareFile convertTo(final DeviceFirmware source, final Type<DeviceFirmwareFile> destinationType,
            final MappingContext mappingContext) {
        final Device device = this.deviceRepository.findByDeviceIdentification(source.getDeviceIdentification());
        final FirmwareFile firmwareFile = this.firmwareFileRepository
                .findOne(Long.valueOf(source.getFirmware().getId()));

        return new DeviceFirmwareFile(device, firmwareFile,
                source.getInstallationDate().toGregorianCalendar().getTime(), source.getInstalledBy());
    }

    @Override
    public DeviceFirmware convertFrom(final DeviceFirmwareFile source, final Type<DeviceFirmware> destinationType,
            final MappingContext mappingContext) {
        final DeviceFirmware destination = new DeviceFirmware();
        destination.setDeviceIdentification(source.getDevice().getDeviceIdentification());

        final Firmware firmware = this.mapperFacade.map(source.getFirmwareFile(), Firmware.class, mappingContext);
        destination.setFirmware(firmware);

        final GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(source.getInstallationDate());

        try {
            destination.setInstallationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar));
        } catch (final DatatypeConfigurationException e) {
            // This won't happen, so no further action is needed.
            LOGGER.error("Bad date format in the installation date", e);
        }

        destination.setInstalledBy(source.getInstalledBy());
        destination.setActive(true);
        return destination;
    }
}