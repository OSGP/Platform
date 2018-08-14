/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device;
import org.opensmartgridplatform.domain.core.entities.Ssld;
import org.opensmartgridplatform.domain.core.valueobjects.Container;
import org.opensmartgridplatform.domain.core.valueobjects.GpsCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

class WsInstallationDeviceToSsldConverter extends CustomConverter<Device, Ssld> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsInstallationDeviceToSsldConverter.class);

    @Override
    public Ssld convert(final Device source, final Type<? extends Ssld> destinationType,
            final MappingContext mappingContext) {

        LOGGER.debug("Converting WS Installation Device into SSLD [{}]", source.getDeviceIdentification());

        final String deviceIdentification = source.getDeviceIdentification();
        final String alias = source.getAlias();
        final Container container = new Container(source.getContainerCity(), source.getContainerPostalCode(),
                source.getContainerStreet(), source.getContainerNumber(), source.getContainerMunicipality());
        final GpsCoordinates gpsCoordinates = new GpsCoordinates(source.getGpsLatitude(), source.getGpsLongitude());
        return new Ssld(deviceIdentification, alias, container, gpsCoordinates, null);
    }

}
