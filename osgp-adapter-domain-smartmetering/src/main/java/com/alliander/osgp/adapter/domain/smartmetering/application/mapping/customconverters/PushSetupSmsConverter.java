/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters;

import java.util.List;
import java.util.Objects;

import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.ConfigurationMapper;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObisCode;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObjectDefinition;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PushSetupSms;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SendDestinationAndMethod;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.WindowElement;
import com.alliander.osgp.dto.valueobjects.smartmetering.CosemObisCodeDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.CosemObjectDefinitionDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.PushSetupSmsDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SendDestinationAndMethodDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.WindowElementDto;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class PushSetupSmsConverter extends BidirectionalConverter<PushSetupSmsDto, PushSetupSms> {

    private final ConfigurationMapper mapper;

    public PushSetupSmsConverter() {
        this.mapper = new ConfigurationMapper();
    }

    public PushSetupSmsConverter(final ConfigurationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PushSetupSmsConverter)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final PushSetupSmsConverter o = (PushSetupSmsConverter) other;
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
    public PushSetupSms convertTo(final PushSetupSmsDto source, final Type<PushSetupSms> destinationType,
            final MappingContext context) {

        if (source == null) {
            return null;
        }

        final CosemObisCode logicalName = this.mapper.map(source.getLogicalName(), CosemObisCode.class);
        final List<CosemObjectDefinition> pushObjectList;
        if (source.getPushObjectList() == null) {
            pushObjectList = null;
        } else {
            pushObjectList = this.mapper.mapAsList(source.getPushObjectList(), CosemObjectDefinition.class);
        }
        final SendDestinationAndMethod sendDestinationAndMethod = this.mapper.map(source.getSendDestinationAndMethod(),
                SendDestinationAndMethod.class);
        final List<WindowElement> communicationWindow;
        if (source.getCommunicationWindow() == null) {
            communicationWindow = null;
        } else {
            communicationWindow = this.mapper.mapAsList(source.getCommunicationWindow(), WindowElement.class);
        }

        return new PushSetupSms(logicalName, pushObjectList, sendDestinationAndMethod, communicationWindow,
                source.getRandomisationStartInterval(), source.getNumberOfRetries(), source.getRepetitionDelay());
    }

    @Override
    public PushSetupSmsDto convertFrom(final PushSetupSms source, final Type<PushSetupSmsDto> destinationType,
            final MappingContext context) {

        if (source == null) {
            return null;
        }

        final CosemObisCodeDto logicalName = this.mapper.map(source.getLogicalName(), CosemObisCodeDto.class);
        final List<CosemObjectDefinitionDto> pushObjectList;
        if (source.getPushObjectList() == null) {
            pushObjectList = null;
        } else {
            pushObjectList = this.mapper.mapAsList(source.getPushObjectList(), CosemObjectDefinitionDto.class);
        }
        final SendDestinationAndMethodDto sendDestinationAndMethod = this.mapper
                .map(source.getSendDestinationAndMethod(), SendDestinationAndMethodDto.class);
        final List<WindowElementDto> communicationWindow;
        if (source.getCommunicationWindow() == null) {
            communicationWindow = null;
        } else {
            communicationWindow = this.mapper.mapAsList(source.getCommunicationWindow(), WindowElementDto.class);
        }

        return (PushSetupSmsDto) PushSetupSmsDto.newBuilder().logicalName(logicalName).pushObjectList(pushObjectList)
                .sendDestinationAndMethod(sendDestinationAndMethod).communicationWindow(communicationWindow)
                .randomisationStartInterval(source.getRandomisationStartInterval())
                .numberOfRetries(source.getNumberOfRetries()).repetitionDelay(source.getRepetitionDelay()).build();
    }
}
