/**
 * Copyright 2018 Smart Society Services B.V.
 */
package com.alliander.osgp.adapter.ws.core.infra.ws;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.alliander.osgp.adapter.ws.core.application.config.NotificationClientConfig;
import com.alliander.osgp.adapter.ws.schema.core.notification.Notification;
import com.alliander.osgp.adapter.ws.schema.core.notification.SendNotificationRequest;
import com.alliander.osgp.shared.exceptionhandling.WebServiceSecurityException;
import com.alliander.osgp.shared.infra.ws.DefaultWebServiceTemplateFactory;

/**
 * SOAP client for the notification web service.
 */
public class SendNotificationServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendNotificationServiceClient.class);

    private final DefaultWebServiceTemplateFactory webServiceTemplateFactory;

    /**
     * An instance of this class is created by a bean function in an application
     * context class. See
     * {@link NotificationClientConfig#sendNotificationServiceClient()}.
     *
     * @param webServiceTemplateFactory
     *            An instance of the web service template factory.
     */
    public SendNotificationServiceClient(final DefaultWebServiceTemplateFactory webServiceTemplateFactory) {
        this.webServiceTemplateFactory = webServiceTemplateFactory;
    }

    public void sendNotification(final String organisationIdentification, final Notification notification)
            throws WebServiceSecurityException {
        final SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setNotification(notification);

        final String notImportantUserName = "OSGP-user";

        final WebServiceTemplate template = this.webServiceTemplateFactory.getTemplate(organisationIdentification,
                notImportantUserName);
        template.marshalSendAndReceive(sendNotificationRequest);
    }

    public void sendNotification(final String organisationIdentification, final Notification notification,
            final String notificationURL, final String notificationUsername) throws WebServiceSecurityException {

        final SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setNotification(notification);

        try {
            this.webServiceTemplateFactory
                    .getTemplate(organisationIdentification, notificationUsername, new URL(notificationURL))
                    .marshalSendAndReceive(sendNotificationRequest);
        } catch (final MalformedURLException e) {
            LOGGER.error("Unexpected exception by creating notification URL", e);
        }
    }
}
