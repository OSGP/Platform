/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.microgrids.application.mapping;

import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.Type;

@Component
public class DomainMicrogridsMapper extends ConfigurableMapper {

    @Override
    protected void configure(final MapperFactory factory) {
        factory.getConverterFactory().registerConverter(new CustomConverter<DateTimeZone, DateTimeZone>() {

            @Override
            public DateTimeZone convert(final DateTimeZone source, final Type<? extends DateTimeZone> destinationType) {
                return DateTimeZone.forID(source.getID());
            }
        });

        factory.getConverterFactory().registerConverter(new CustomConverter<ISOChronology, ISOChronology>() {

            @Override
            public ISOChronology convert(final ISOChronology source,
                    final Type<? extends ISOChronology> destinationType) {
                return ISOChronology.getInstance(source.getZone());
            }
        });
    }
}
