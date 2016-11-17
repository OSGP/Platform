/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.microgrids.infra.jms.core;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.alliander.osgp.shared.infra.jms.Constants;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

// This class should send response messages to OSGP Core.
@Component(value = "domainMicrogridsOutgoingOsgpCoreResponseMessageSender")
public class OsgpCoreResponseMessageSender {

    @Autowired
    @Qualifier("domainMicrogridsOutgoingOsgpCoreResponsesJmsTemplate")
    private JmsTemplate osgpCoreResponsesJmsTemplate;

    public void send(final ResponseMessage responseMessage, final String messageType) {

        this.osgpCoreResponsesJmsTemplate.send(new MessageCreator() {

            @Override
            public Message createMessage(final Session session) throws JMSException {
                final ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setJMSType(messageType);
                objectMessage.setStringProperty(Constants.ORGANISATION_IDENTIFICATION,
                        responseMessage.getOrganisationIdentification());
                objectMessage.setStringProperty(Constants.DEVICE_IDENTIFICATION,
                        responseMessage.getDeviceIdentification());
                objectMessage.setObject(responseMessage);
                return objectMessage;
            }
        });
    }
}
