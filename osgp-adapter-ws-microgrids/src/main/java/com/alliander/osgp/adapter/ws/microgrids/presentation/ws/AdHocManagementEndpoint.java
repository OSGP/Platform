/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.microgrids.presentation.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.adapter.ws.microgrids.application.exceptionhandling.ResponseNotFoundException;
import com.alliander.osgp.adapter.ws.microgrids.application.mapping.MicrogridsMapper;
import com.alliander.osgp.adapter.ws.microgrids.application.services.MicrogridsService;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataRequest;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataResponse;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.SetSetPointsAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.SetSetPointsAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.SetSetPointsRequest;
import com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.SetSetPointsResponse;
import com.alliander.osgp.adapter.ws.schema.microgrids.common.AsyncResponse;
import com.alliander.osgp.adapter.ws.schema.microgrids.common.OsgpResultType;
import com.alliander.osgp.domain.microgrids.valueobjects.DataRequest;
import com.alliander.osgp.domain.microgrids.valueobjects.DataResponse;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.exceptionhandling.TechnicalException;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

@Endpoint
public class AdHocManagementEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdHocManagementEndpoint.class);
    private static final String NAMESPACE = "http://www.alliander.com/schemas/osgp/microgrids/adhocmanagement/2016/06";

    @Autowired
    private MicrogridsService service;

    @Autowired
    private MicrogridsMapper mapper;

    // === GET DATA ===

    @PayloadRoot(localPart = "GetDataRequest", namespace = NAMESPACE)
    @ResponsePayload
    public GetDataAsyncResponse getData(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final GetDataRequest request) throws OsgpException {

        LOGGER.info("Get Status received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());

        final GetDataAsyncResponse response = new GetDataAsyncResponse();

        try {
            final DataRequest dataRequest = this.mapper.map(request, DataRequest.class);
            final String correlationUid = this.service.enqueueGetDataRequest(organisationIdentification,
                    request.getDeviceIdentification(), dataRequest);

            final AsyncResponse asyncResponse = new AsyncResponse();
            asyncResponse.setCorrelationUid(correlationUid);
            asyncResponse.setDeviceId(request.getDeviceIdentification());
            response.setAsyncResponse(asyncResponse);
        } catch (final Exception e) {
            this.handleException(e);
        }

        return response;
    }

    @PayloadRoot(localPart = "GetDataAsyncRequest", namespace = NAMESPACE)
    @ResponsePayload
    public GetDataResponse getGetDataResponse(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final GetDataAsyncRequest request) throws OsgpException {

        LOGGER.info("Get Status Response received from organisation: {} for correlationUid: {}.",
                organisationIdentification, request.getAsyncRequest().getCorrelationUid());

        GetDataResponse response = new GetDataResponse();

        try {

            final DataResponse dataResponse = this.service
                    .dequeueGetDataResponse(request.getAsyncRequest().getCorrelationUid());
            if (dataResponse != null) {
                response = this.mapper.map(dataResponse, GetDataResponse.class);

            } else {
                // Temporary mock response, without a queue
                response.setResult(OsgpResultType.NOT_FOUND);
            }

        } catch (final ResponseNotFoundException e) {
            response.setResult(OsgpResultType.NOT_FOUND);
        } catch (final Exception e) {
            this.handleException(e);
        }

        return response;
    }

    // === SET TRANSITION ===

    @PayloadRoot(localPart = "SetSetPointsRequest", namespace = NAMESPACE)
    @ResponsePayload
    public SetSetPointsAsyncResponse setSetPoints(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final SetSetPointsRequest request) throws OsgpException {

        LOGGER.info("Set Transition Request received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());

        final SetSetPointsAsyncResponse response = new SetSetPointsAsyncResponse();

        try {
            // TODO - Replace object with "real SetPointsRequestData" class
            final Object requestData = null;
            final String correlationUid = this.service.enqueueSetSetPointsRequest(organisationIdentification,
                    request.getDeviceIdentification(), requestData);

            final AsyncResponse asyncResponse = new AsyncResponse();
            asyncResponse.setCorrelationUid(correlationUid);
            asyncResponse.setDeviceId(request.getDeviceIdentification());
            response.setAsyncResponse(asyncResponse);
        } catch (final Exception e) {
            this.handleException(e);
        }
        return response;
    }

    @PayloadRoot(localPart = "SetSetPointsAsyncRequest", namespace = NAMESPACE)
    @ResponsePayload
    public SetSetPointsResponse getSetSetPointsResponse(
            @OrganisationIdentification final String organisationIdentification,
            @RequestPayload final SetSetPointsAsyncRequest request) throws OsgpException {

        LOGGER.info("Get Set Transition Response received from organisation: {} with correlationUid: {}.",
                organisationIdentification, request.getAsyncRequest().getCorrelationUid());

        final SetSetPointsResponse response = new SetSetPointsResponse();

        try {
            final ResponseMessage message = this.service
                    .dequeueSetSetPointsResponse(request.getAsyncRequest().getCorrelationUid());
            if (message != null) {
                response.setResult(OsgpResultType.fromValue(message.getResult().getValue()));
            }

        } catch (final Exception e) {
            this.handleException(e);
        }
        return response;
    }

    private void handleException(final Exception e) throws OsgpException {
        // Rethrow exception if it already is a functional or technical
        // exception, otherwise throw new technical exception.
        LOGGER.error("Exception occurred: ", e);
        if (e instanceof OsgpException) {
            throw (OsgpException) e;
        } else {
            throw new TechnicalException(ComponentType.WS_MICROGRIDS, e);
        }
    }
}