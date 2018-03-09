/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.ws.domain.entities.ResponseData;
import com.alliander.osgp.adapter.ws.domain.repositories.ResponseDataRepository;
import com.alliander.osgp.adapter.ws.schema.shared.notification.GenericSendNotificationRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.NotificationType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.SendNotificationRequest;
import com.alliander.osgp.adapter.ws.shared.services.AbstractNotificationServiceWs;
import com.alliander.osgp.adapter.ws.shared.services.NotificationService;
import com.alliander.osgp.adapter.ws.shared.services.ResponseUrlService;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Transactional(value = "transactionManager")
@Validated
public class NotificationServiceWs extends AbstractNotificationServiceWs implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceWs.class);

    @Autowired
    private ResponseUrlService responseUrlService;

    @Autowired
    private ResponseDataRepository responseDataRepository;

    private final DefaultWebServiceTemplateFactory webServiceTemplateFactory;
    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public NotificationServiceWs(final DefaultWebServiceTemplateFactory webServiceTemplateFactory,
            final String notificationUrl, final String notificationUsername, final String notificationOrganisation) {
        super(notificationUrl, notificationUsername, notificationOrganisation);
        this.webServiceTemplateFactory = webServiceTemplateFactory;
    }

    @Override
    public void sendNotification(final String organisationIdentification, final String deviceIdentification,
            final String result, final String correlationUid, final String message, final Object notificationType) {

        LOGGER.info("sendNotification called with organisation: {}, correlationUid: {}, type: {}, to organisation: {}",
                this.notificationOrganisation, correlationUid, notificationType, organisationIdentification);

        // try to get the response url from the response data
        final ResponseData responseData = this.responseDataRepository.findByCorrelationUid(correlationUid);
        String notifyUrl = responseData.getResponseUrl();
        if (notifyUrl == null) {
            // handling the first notification, retrieve the url form the
            // response url
            notifyUrl = this.retrieveNotificationUrl(this.responseUrlService, correlationUid);

            // store the url in the response data so it won't be lost
            responseData.setResponseUrl(notifyUrl);
            this.responseDataRepository.save(responseData);
        }

        final GenericSendNotificationRequest genericNotificationRequest = this.genericNotificationRequest(
                deviceIdentification, result, correlationUid, message,
                ((NotificationType) notificationType).toString());
        final SendNotificationRequest notificationRequest = this.mapperFactory.getMapperFacade()
                .map(genericNotificationRequest, SendNotificationRequest.class);
        this.doSendNotification(this.webServiceTemplateFactory, organisationIdentification, this.notificationUsername,
                notifyUrl, notificationRequest);
    }

}
