/**
 * Copyright 2018 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.core.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alliander.osgp.adapter.ws.core.infra.ws.SendNotificationServiceClient;
import com.alliander.osgp.adapter.ws.schema.core.notification.Notification;
import com.alliander.osgp.adapter.ws.schema.core.notification.NotificationType;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SendNotificationServiceClient sendNotificationServiceClient;

    public void sendNotification(final NotificationType notificationType, final String organisationIdentification,
            final String deviceIdentification) {
        final Notification notification = new Notification();
        notification.setNotificationType(notificationType);
        notification.setDeviceIdentification(deviceIdentification);
        try {
            this.sendNotificationServiceClient.sendNotification(organisationIdentification, notification);
        } catch (final WebServiceSecurityException e) {
            LOGGER.error("Unable to send notification", e);
        }
    }
}
