/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.tariffswitching.application.mapping.ws;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.TariffSchedule;
import com.alliander.osgp.adapter.ws.schema.tariffswitching.schedulemanagement.TariffValue;
import com.alliander.osgp.domain.core.exceptions.ValidationException;
import com.alliander.osgp.domain.core.valueobjects.ActionTimeType;
import com.alliander.osgp.domain.core.valueobjects.LightValue;
import com.alliander.osgp.domain.core.valueobjects.ScheduleEntry;
import com.alliander.osgp.domain.core.valueobjects.WeekDayType;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class TariffScheduleToScheduleConverter extends CustomConverter<TariffSchedule, ScheduleEntry> {

    @Override
    public ScheduleEntry convert(final TariffSchedule source, final Type<? extends ScheduleEntry> destinationType,
            final MappingContext context) {
        final ScheduleEntry schedule = new ScheduleEntry();

        // Copy values
        schedule.setWeekDay(this.mapperFacade.map(source.getWeekDay(), WeekDayType.class));
        schedule.setStartDay(this.mapperFacade.map(source.getStartDay(), DateTime.class));
        schedule.setEndDay(this.mapperFacade.map(source.getEndDay(), DateTime.class));
        schedule.setTime(source.getTime());
        schedule.setIndex(source.getIndex());
        schedule.setIsEnabled(source.isIsEnabled());
        schedule.setMinimumLightsOn(source.getMinimumLightsOn());

        try {
            // Set the lightvalue
            // For now a High tariff means the Relay is switched off (Situation
            // in Zaltbommel)
            // schedule.setLightValue( Arrays.asList(new LightValue(null,
            // !source.isHigh(), null)) )

            final List<LightValue> lightValues = new ArrayList<>();
            for (final TariffValue tariffValue : source.getTariffValue()) {

                final LightValue lightValue = new LightValue(tariffValue.getIndex(), !tariffValue.isHigh(), null);
                lightValues.add(lightValue);
            }

            schedule.setLightValue(lightValues);

        } catch (final ValidationException e) {
            throw new IllegalArgumentException(e);
        }

        // Set defaults for Tariff schedules
        schedule.setActionTime(ActionTimeType.ABSOLUTETIME);
        schedule.setTriggerType(null);

        return schedule;
    }

}
