/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping;

import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters.EventMessageDataResponseConverter;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component(value = "managementMapper")
public class ManagementMapper extends ConfigurableMapper {
    @Override
    public void configure(final MapperFactory mapperFactory) {
        // These two converters are needed. Otherwise mapping will lead to
        // strange exceptions when mapping DateTime fields
        mapperFactory.getConverterFactory().registerConverter(new FindEventsRequestDataConverter());
        mapperFactory.getConverterFactory().registerConverter(new EventsConverter());
        mapperFactory.getConverterFactory().registerConverter(new EventMessageDataResponseConverter());
    }
}
