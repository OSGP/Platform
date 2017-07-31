/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.core.application.mapping;

import java.util.Objects;

import com.alliander.osgp.domain.core.entities.LightMeasurementDevice;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class LightMeasurementDeviceConverter extends
        BidirectionalConverter<LightMeasurementDevice, com.alliander.osgp.adapter.ws.schema.core.devicemanagement.Device> {

    private final DeviceConverterHelper<LightMeasurementDevice> helper = new DeviceConverterHelper<>(
            LightMeasurementDevice.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BidirectionalConverter#convertTo(java.lang
     * .Object, ma.glasnost.orika.metadata.Type,
     * ma.glasnost.orika.MappingContext)
     */
    @Override
    public com.alliander.osgp.adapter.ws.schema.core.devicemanagement.Device convertTo(
            final LightMeasurementDevice source,
            final Type<com.alliander.osgp.adapter.ws.schema.core.devicemanagement.Device> destinationType,
            final MappingContext mappingContext) {
        return this.helper.initJaxb(source);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BidirectionalConverter#convertFrom(java.lang
     * .Object, ma.glasnost.orika.metadata.Type,
     * ma.glasnost.orika.MappingContext)
     */
    @Override
    public LightMeasurementDevice convertFrom(
            final com.alliander.osgp.adapter.ws.schema.core.devicemanagement.Device source,
            final Type<LightMeasurementDevice> destinationType, final MappingContext mappingContext) {
        return this.helper.initEntity(source);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(this.helper);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
