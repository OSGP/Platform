/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import java.util.Objects;

import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

class DeviceConverter extends BidirectionalConverter<org.opensmartgridplatform.domain.core.entities.Device, Device> {

    private final DeviceConverterHelper<org.opensmartgridplatform.domain.core.entities.Device> helper = new DeviceConverterHelper<>(
            org.opensmartgridplatform.domain.core.entities.Device.class);

    @Override
    public org.opensmartgridplatform.domain.core.entities.Device convertFrom(
            final org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device source,
            final Type<org.opensmartgridplatform.domain.core.entities.Device> destinationType, final MappingContext context) {
        return this.helper.initEntity(source);
    }

    @Override
    public org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device convertTo(
            final org.opensmartgridplatform.domain.core.entities.Device source,
            final Type<org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device> destinationType,
            final MappingContext context) {
        return this.helper.initJaxb(source);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hash(this.helper);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && Objects.equals(this.helper, ((DeviceConverter) obj).helper);
    }

}