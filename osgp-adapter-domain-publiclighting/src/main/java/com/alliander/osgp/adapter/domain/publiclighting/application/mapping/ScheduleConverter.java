/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.publiclighting.application.mapping;

import com.alliander.osgp.domain.core.valueobjects.Schedule;
import com.alliander.osgp.domain.core.valueobjects.ScheduleEntry;
import com.alliander.osgp.dto.valueobjects.ScheduleDto;
import com.alliander.osgp.dto.valueobjects.ScheduleEntryDto;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class ScheduleConverter extends BidirectionalConverter<ScheduleDto, Schedule> {

    @Override
    public Schedule convertTo(final ScheduleDto source, final Type<Schedule> destinationType,
            final MappingContext context) {

        return new Schedule(this.mapperFacade.mapAsList(source.getScheduleList(), ScheduleEntry.class),
                source.getAstronomicalSunriseOffset(), source.getAstronomicalSunsetOffset());
    }

    @Override
    public ScheduleDto convertFrom(final Schedule source, final Type<ScheduleDto> destinationType,
            final MappingContext context) {

        return new ScheduleDto(this.mapperFacade.mapAsList(source.getScheduleEntries(), ScheduleEntryDto.class),
                source.getAstronomicalSunriseOffset(), source.getAstronomicalSunsetOffset());

    }

}
