/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import java.util.List;

import javax.annotation.PostConstruct;

import org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable.WritableDeviceModelRepository;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.DeviceModel;
import org.opensmartgridplatform.domain.core.repositories.SsldRepository;
import org.opensmartgridplatform.domain.core.valueobjects.Container;
import org.opensmartgridplatform.domain.core.valueobjects.GpsCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.Type;

@Component(value = "coreDeviceInstallationMapper")
public class DeviceInstallationMapper extends ConfigurableMapper {

    @Autowired
    private SsldRepository ssldRepository;

    @Autowired
    private WritableDeviceModelRepository writableDeviceModelRepository;

    public DeviceInstallationMapper() {
        super(false);
    }

    @PostConstruct
    public void initialize() {
        this.init();
    }

    @Override
    public void configure(final MapperFactory mapperFactory) {
        mapperFactory.getConverterFactory()
                .registerConverter(new DeviceConverter(this.ssldRepository, this.writableDeviceModelRepository));
        mapperFactory.getConverterFactory().registerConverter(new WsInstallationDeviceToSsldConverter());
    }

    private static class DeviceConverter extends
            BidirectionalConverter<Device, org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device> {

        private SsldRepository ssldRepository;

        private WritableDeviceModelRepository writableDeviceModelRepository;

        public DeviceConverter(final SsldRepository ssldRepository,
                final WritableDeviceModelRepository writableDeviceModelRepository) {
            this.ssldRepository = ssldRepository;
            this.writableDeviceModelRepository = writableDeviceModelRepository;
        }

        @Override
        public Device convertFrom(
                final org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device source,
                final Type<Device> destinationType, final MappingContext context) {

            Device destination = null;

            if (source != null) {
                destination = new Device(source.getDeviceIdentification(), source.getAlias(),
                        new Container(source.getContainerCity(), source.getContainerPostalCode(),
                                source.getContainerStreet(), source.getContainerNumber(),
                                source.getContainerMunicipality()),
                        new GpsCoordinates(source.getGpsLatitude(), source.getGpsLongitude()), null);

                /*
                 * Model code does not uniquely identify a device model, which
                 * is why deviceModelRepository is changed to return a list of
                 * device models.
                 *
                 * A better solution would be to determine the manufacturer and
                 * do a lookup by manufacturer and model code, which should
                 * uniquely define the device model.
                 */
                final List<DeviceModel> deviceModels = this.writableDeviceModelRepository
                        .findByModelCode(source.getDeviceModel().getModelCode());

                if (deviceModels.size() > 1) {
                    throw new AssertionError("Model code \"" + source.getDeviceModel().getModelCode()
                            + "\" does not uniquely identify a device model.");
                }
                if (!deviceModels.isEmpty()) {
                    destination.setDeviceModel(deviceModels.get(0));
                }

                return destination;
            }
            return null;
        }

        @Override
        public org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device convertTo(final Device source,
                final Type<org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device> destinationType,
                final MappingContext context) {

            org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device destination = null;
            if (source != null) {
                destination = new org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device();
                destination.setDeviceIdentification(source.getDeviceIdentification());
                destination.setAlias(source.getAlias());
                destination.setContainerCity(source.getContainer().getCity());
                destination.setContainerPostalCode(source.getContainer().getPostalCode());
                destination.setContainerStreet(source.getContainer().getStreet());
                destination.setContainerNumber(source.getContainer().getNumber());
                destination.setContainerMunicipality(source.getContainer().getMunicipality());
                destination.setGpsLatitude(source.getGpsCoordinates().getLatitude());
                destination.setGpsLongitude(source.getGpsCoordinates().getLongitude());

                destination.setActivated(source.isActivated());
                destination.setHasSchedule(this.ssldRepository.findOne(source.getId()).getHasSchedule());

                return destination;
            }
            return null;
        }
    }
}
