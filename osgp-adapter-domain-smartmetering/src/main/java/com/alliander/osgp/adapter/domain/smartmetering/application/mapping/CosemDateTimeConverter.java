/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping;

import java.util.Objects;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.ClockStatus;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemDate;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemDateTime;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemTime;
import com.alliander.osgp.dto.valueobjects.smartmetering.ClockStatusDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.CosemDateDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.CosemDateTimeDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.CosemTimeDto;

public class CosemDateTimeConverter extends BidirectionalConverter<CosemDateTimeDto, CosemDateTime> {

    private final ConfigurationMapper mapper;

    public CosemDateTimeConverter() {
        this.mapper = new ConfigurationMapper();
    }

    public CosemDateTimeConverter(final ConfigurationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CosemDateTimeConverter)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final CosemDateTimeConverter o = (CosemDateTimeConverter) other;
        if (this.mapper == null) {
            return o.mapper == null;
        }
        return this.mapper.getClass().equals(o.mapper.getClass());
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Objects.hashCode(this.mapper);
    }

    @Override
    public CosemDateTime convertTo(final CosemDateTimeDto source, final Type<CosemDateTime> destinationType) {
        if (source == null) {
            return null;
        }

        final ClockStatus clockStatus = new ClockStatus(source.getClockStatus().getStatus());

        return new CosemDateTime(this.mapper.map(source.getDate(), CosemDate.class), this.mapper.map(source.getTime(),
                CosemTime.class), source.getDeviation(), clockStatus);
    }

    @Override
    public CosemDateTimeDto convertFrom(final CosemDateTime source, final Type<CosemDateTimeDto> destinationType) {
        if (source == null) {
            return null;
        }

        final ClockStatusDto clockStatus = new ClockStatusDto(source.getClockStatus().getStatus());

        return new CosemDateTimeDto(this.mapper.map(source.getDate(), CosemDateDto.class), this.mapper.map(
                source.getTime(), CosemTimeDto.class), source.getDeviation(), clockStatus);
    }
}
