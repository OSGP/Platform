/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.publiclighting.application.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alliander.osgp.adapter.domain.publiclighting.infra.jms.core.OsgpCoreRequestMessageSender;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.Manufacturer;
import com.alliander.osgp.domain.core.repositories.DeviceModelRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.EventRepository;
import com.alliander.osgp.domain.core.repositories.ManufacturerRepository;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.dto.valueobjects.DomainTypeDto;
import com.alliander.osgp.shared.infra.jms.RequestMessage;

/**
 * Base class for scheduled tasks.
 */
public class BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTask.class);

    @Autowired
    @Qualifier("domainPublicLightingOutgoingOsgpCoreRequestMessageSender")
    protected OsgpCoreRequestMessageSender osgpCoreRequestMessageSender;

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected ManufacturerRepository manufacturerRepository;

    @Autowired
    protected DeviceModelRepository deviceModelRepository;

    @Autowired
    protected EventRepository eventRepository;

    /**
     * Try to find a manufacturer by name (case sensitive).
     */
    protected Manufacturer findManufacturer(final String name) {
        LOGGER.info("Trying to find manufacturer for name: {}", name);
        final Manufacturer manufacturer = this.manufacturerRepository.findByName(name);
        if (manufacturer == null) {
            LOGGER.warn("No manufacturer found for name: {}", name);
        } else {
            LOGGER.info("Manufacturer found for name: {}", name);
        }
        return manufacturer;
    }

    /**
     * Try to find all device models for a manufacturer.
     */
    protected List<DeviceModel> findDeviceModels(final Manufacturer manufacturer) {
        LOGGER.info("Trying to find device models for manufacturer: {}", manufacturer.getName());
        final List<DeviceModel> deviceModels = this.deviceModelRepository.findByManufacturerId(manufacturer);
        if (deviceModels == null) {
            LOGGER.warn("No device models found for manufacturer with name: {}, deviceModels == null",
                    manufacturer.getName());
        } else if (deviceModels.isEmpty()) {
            LOGGER.warn("No device models found for manufacturer with name: {}, deviceModels.isEmpty()",
                    manufacturer.getName());
        } else {
            LOGGER.info("{} device models found for manufacturer with name: {}", deviceModels.size(),
                    manufacturer.getName());
            for (final DeviceModel deviceModel : deviceModels) {
                LOGGER.info(" deviceModel: {}", deviceModel.getModelCode());
            }
        }
        return deviceModels;
    }

    /**
     * Try to find all devices which are not 'in maintenance' for a list of
     * device models.
     */
    protected List<Device> findDevices(final List<DeviceModel> deviceModels, final String deviceType) {
        LOGGER.info("Trying to find devices for device models for manufacturer...");
        final List<Device> devices = new ArrayList<>();
        for (final DeviceModel deviceModel : deviceModels) {
            final List<Device> devs = this.deviceRepository.findByDeviceModelAndDeviceTypeAndInMaintenanceAndIsActive(
                    deviceModel, deviceType, false, true);
            devices.addAll(devs);
        }
        if (devices.isEmpty()) {
            LOGGER.warn("No devices found for device models for manufacturer");
        } else {
            LOGGER.info("{} devices found for device models for manufacturer", devices.size());
            for (final Device device : devices) {
                LOGGER.info(" device: {}", device.getDeviceIdentification());
            }
        }
        return devices;
    }

    /**
     * Filter a list of given devices to determine which devices should be
     * contacted. The filtering uses the age of the latest event in comparison
     * with 'maximumAllowedAge'.
     */
    protected List<Device> findDevicesToContact(final List<Device> devices, final int maximumAllowedAge) {
        List<Object> listOfObjectArrays = this.eventRepository.findLatestEventForEveryDevice(devices);
        LOGGER.info("listOfObjectArrays.size(): {}", listOfObjectArrays.size());

        final Date maxAge = DateTime.now(DateTimeZone.UTC).minusHours(maximumAllowedAge).toDate();
        LOGGER.info("maxAge: {}", maxAge);

        final Map<Long, Date> map = new HashMap<>();
        for (final Object objectArray : listOfObjectArrays) {
            final Object[] array = (Object[]) objectArray;
            final Long eventDeviceId = (Long) array[0];
            final Date timestamp = (Date) array[1];
            LOGGER.info("eventDeviceId: {}, timestamp: {}", eventDeviceId, timestamp);
            if (this.isEventOlderThanMaxInterval(maxAge, timestamp, maximumAllowedAge)) {
                map.put(eventDeviceId, timestamp);
            }
        }

        listOfObjectArrays = null;

        final List<Device> devicesToContact = this.deviceRepository.findAll(map.keySet());
        for (final Device device : devicesToContact) {
            LOGGER.info("device: {}, id: {}", device.getDeviceIdentification(), device.getId());
        }
        return devicesToContact;
    }

    /**
     * Determine if an event is older than X hours as indicated by maxAge.
     */
    protected boolean isEventOlderThanMaxInterval(final Date maxAge, final Date event, final int maximumAllowedAge) {
        if (event == null) {
            // In case the event instance is null, try to contact the device.
            LOGGER.info("Event instance is null");
            return true;
        }
        final boolean result = event.before(maxAge);
        LOGGER.info("event date time: {}, current date time minus {} hours: {}, is event before? : {}", event,
                maximumAllowedAge, maxAge, result);
        return result;
    }

    protected void contactDevices(final List<Device> devicesToContact, final DeviceFunction deviceFunction) {
        for (final Device device : devicesToContact) {
            this.sendRequestMessageToDevice(device, deviceFunction);
        }
    }

    protected void sendRequestMessageToDevice(final Device device, final DeviceFunction deviceFunction) {
        final String deviceIdentification = device.getDeviceIdentification();
        // Try to use the identification of the owner organization.
        final String organisation = device.getOwner() == null ? "" : device.getOwner().getOrganisationIdentification();
        // Creating message with empty CorrelationUID, in order to prevent a
        // response.
        final String correlationUid = "";
        final String deviceFunctionString = deviceFunction.name();
        final DomainTypeDto domain = DomainTypeDto.PUBLIC_LIGHTING;

        String ipAddress = null;
        if (device.getNetworkAddress() == null) {
            // In case the device does not have a known IP address, don't send
            // a request message.
            LOGGER.warn("Unable to create protocol request message because the IP address is empty for device: {}",
                    deviceIdentification);
            return;
        } else {
            ipAddress = device.getNetworkAddress().getHostAddress();
        }

        final RequestMessage requestMessage = new RequestMessage(correlationUid, organisation, deviceIdentification,
                domain);

        this.osgpCoreRequestMessageSender.send(requestMessage, deviceFunctionString, ipAddress);
    }

}
