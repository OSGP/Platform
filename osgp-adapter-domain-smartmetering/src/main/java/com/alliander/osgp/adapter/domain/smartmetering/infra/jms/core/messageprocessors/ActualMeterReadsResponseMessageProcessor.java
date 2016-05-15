/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.infra.jms.core.messageprocessors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alliander.osgp.adapter.domain.smartmetering.application.services.MonitoringService;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageProcessor;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.dto.valueobjects.smartmetering.MeterReadsResponseDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.MeterReadsGasResponseDto;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.infra.jms.DeviceMessageMetadata;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

@Component
public class ActualMeterReadsResponseMessageProcessor extends OsgpCoreResponseMessageProcessor {

    @Autowired
    private MonitoringService monitoringService;

    protected ActualMeterReadsResponseMessageProcessor() {
        super(DeviceFunction.REQUEST_ACTUAL_METER_DATA);
    }

    @Override
    protected boolean hasRegularResponseObject(final ResponseMessage responseMessage) {
        final Object dataObject = responseMessage.getDataObject();
        return dataObject instanceof MeterReadsResponseDto || dataObject instanceof MeterReadsGasResponseDto;
    }

    @Override
    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata,
            final ResponseMessage responseMessage, final OsgpException osgpException) {

        if (responseMessage.getDataObject() instanceof MeterReadsResponseDto) {
            final MeterReadsResponseDto actualMeterReadsDto = (MeterReadsResponseDto) responseMessage.getDataObject();

            this.monitoringService.handleActualMeterReadsResponse(deviceMessageMetadata, responseMessage.getResult(),
                    osgpException, actualMeterReadsDto);
        } else if (responseMessage.getDataObject() instanceof MeterReadsGasResponseDto) {
            final MeterReadsGasResponseDto meterReadsGas = (MeterReadsGasResponseDto) responseMessage.getDataObject();
            this.monitoringService.handleActualMeterReadsResponse(deviceMessageMetadata, responseMessage.getResult(),
                    osgpException, meterReadsGas);
        }
    }
}
