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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.alliander.osgp.adapter.ws.endpointinterceptors.OrganisationIdentification;
import com.alliander.osgp.adapter.ws.microgrids.application.mapping.AdHocManagementMapper;
import com.alliander.osgp.adapter.ws.microgrids.application.services.AdHocManagementService;
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
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.exceptionhandling.TechnicalException;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

//MethodConstraintViolationException is deprecated.
//Will by replaced by equivalent functionality defined
//by the Bean Validation 1.1 API as of Hibernate Validator 5.
@SuppressWarnings("deprecation")
@Endpoint
public class AdHocManagementEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdHocManagementEndpoint.class);
    private static final String NAMESPACE = "http://www.alliander.com/schemas/osgp/microgrids/adhocmanagement/2016/06";
    private static final ComponentType COMPONENT_WS_MICROGRIDS = ComponentType.WS_MICROGRIDS;

    private static final String EXCEPTION_OCCURRED = "Exception Occurred";

    private final AdHocManagementService adHocManagementService;
    private final AdHocManagementMapper adHocManagementMapper;

    @Autowired
    public AdHocManagementEndpoint(
            @Qualifier("wsMicrogridsAdHocManagementService") final AdHocManagementService adHocManagementService,
            @Qualifier("microgridsAdhocManagementMapper") final AdHocManagementMapper adHocManagementMapper) {
        this.adHocManagementService = adHocManagementService;
        this.adHocManagementMapper = adHocManagementMapper;
    }

    // === GET DATA ===

    @PayloadRoot(localPart = "GetDataRequest", namespace = NAMESPACE)
    @ResponsePayload
    public GetDataAsyncResponse getData(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final GetDataRequest request) throws OsgpException {

        LOGGER.info("Get Status received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());

        final GetDataAsyncResponse response = new GetDataAsyncResponse();

        try {
            // TODO replace object by "real request data" class
            final Object requestData = null;
            final String correlationUid = this.adHocManagementService.enqueueGetDataRequest(organisationIdentification,
                    request.getDeviceIdentification(), null);

            final AsyncResponse asyncResponse = new AsyncResponse();
            asyncResponse.setCorrelationUid(correlationUid);
            asyncResponse.setDeviceId(request.getDeviceIdentification());
            response.setAsyncResponse(asyncResponse);
        } catch (final Exception e) {
            this.handleException(e);
        }

        return response;
    }

    @PayloadRoot(localPart = "GetStatusAsyncRequest", namespace = NAMESPACE)
    @ResponsePayload
    public GetDataResponse getGetDataResponse(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final GetDataAsyncRequest request) throws OsgpException {

        LOGGER.info("Get Status Response received from organisation: {} for correlationUid: {}.",
                organisationIdentification, request.getAsyncRequest().getCorrelationUid());

        final GetDataResponse response = new GetDataResponse();

        try {
            final ResponseMessage message = this.adHocManagementService
                    .dequeueGetStatusResponse(request.getAsyncRequest().getCorrelationUid());
            if (message != null) {
                response.setResult(OsgpResultType.fromValue(message.getResult().getValue()));

                // if (message.getDataObject() != null) {
                // final DeviceStatus deviceStatus = (DeviceStatus)
                // message.getDataObject();
                // if (deviceStatus != null) {
                // response.setDeviceStatus(this.adHocManagementMapper.map(deviceStatus,
                // com.alliander.osgp.adapter.ws.schema.publiclighting.adhocmanagement.DeviceStatus.class));
                // }
                // }
            }
        } catch (final Exception e) {
            this.handleException(e);
        }

        return response;
    }

    // === SET TRANSITION ===

    @PayloadRoot(localPart = "SetSetPointsRequest", namespace = NAMESPACE)
    @ResponsePayload
    public SetSetPointsAsyncResponse setTransition(@OrganisationIdentification final String organisationIdentification,
            @RequestPayload final SetSetPointsRequest request) throws OsgpException {

        LOGGER.info("Set Transition Request received from organisation: {} for device: {}.", organisationIdentification,
                request.getDeviceIdentification());

        final SetSetPointsAsyncResponse response = new SetSetPointsAsyncResponse();

        try {
            // final TransitionMessageDataContainer
            // transitionMessageDataContainer = new
            // TransitionMessageDataContainer();

            // if (request.getTransitionType() != null) {
            // transitionMessageDataContainer.setTransitionType(this.adHocManagementMapper.map(
            // request.getTransitionType(),
            // com.alliander.osgp.domain.core.valueobjects.TransitionType.class));
            // }
            // DateTime dateTime = null;
            // if (request.getTime() != null) {
            // dateTime = new
            // DateTime(request.getTime().toGregorianCalendar().getTime());
            // }
            // transitionMessageDataContainer.setDateTime(dateTime);

            // TODO - Replace object with "real SetPointsRequestData" class
            final Object requestData = null;
            final String correlationUid = this.adHocManagementService.enqueueSetSetPointsRequest(
                    organisationIdentification, request.getDeviceIdentification(), requestData);

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
            final ResponseMessage message = this.adHocManagementService
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
        // exception,
        // otherwise throw new technical exception.
        LOGGER.error("Exception occurred: ", e);
        if (e instanceof OsgpException) {
            throw (OsgpException) e;
        } else {
            throw new TechnicalException(COMPONENT_WS_MICROGRIDS, e);
        }
    }
}