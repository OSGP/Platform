/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.publiclighting.application.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.publiclighting.application.config.SchedulingConfig;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.Manufacturer;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;

/**
 * Periodic task to fetch events from devices of a manufacturer in case the
 * devices have events older than X hours. This ensures all devices are
 * contacted, and are allowed to send any new events in their buffers. See
 * {@link SchedulingConfig#eventRetrievalScheduledTaskCronTrigger()} and
 * {@link SchedulingConfig#eventRetrievalScheduler()}.
 */
@Component
public class DeviceConnectionScheduledTask extends BaseTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceConnectionScheduledTask.class);

    @Autowired
    private String deviceConnectionScheduledTaskManufacturerName;

    @Autowired
    private int deviceConnectionScheduledTaskMaximumAllowedAge;

    @Override
    public void run() {
        try {
            final Manufacturer manufacturer = this.findManufacturer(this.deviceConnectionScheduledTaskManufacturerName);
            if (manufacturer == null) {
                return;
            }

            final List<DeviceModel> deviceModels = this.findDeviceModels(manufacturer);
            if (deviceModels == null || deviceModels.isEmpty()) {
                return;
            }

            final List<Device> devices = this.findDevices(deviceModels);
            if (devices.isEmpty()) {
                return;
            }

            final List<Device> devicesToContact = this.findDevicesToContact(devices,
                    this.deviceConnectionScheduledTaskMaximumAllowedAge);
            if (devicesToContact.isEmpty()) {
                return;
            }

            this.contactDevices(devicesToContact, DeviceFunction.GET_STATUS);
        } catch (final Exception e) {
            LOGGER.error("Exception caught during DeviceConnectionScheduledTask.run()", e);
        }
    }

}
