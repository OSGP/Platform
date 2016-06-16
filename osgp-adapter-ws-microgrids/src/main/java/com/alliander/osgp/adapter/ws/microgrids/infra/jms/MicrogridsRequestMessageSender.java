/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.microgrids.infra.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alliander.osgp.domain.core.exceptions.ArgumentNullOrEmptyException;
import com.alliander.osgp.shared.infra.jms.Constants;

/**
 * Class for sending public lighting request messages to a queue
 */
public class MicrogridsRequestMessageSender {
    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MicrogridsRequestMessageSender.class);

    /**
     * Autowired field for public lighting requests jms template
     */
    @Autowired
    @Qualifier("wsMicrogridsOutgoingRequestsJmsTemplate")
    private JmsTemplate microgridsRequestsJmsTemplate;

    /**
     * Method for sending a request message to the queue
     *
     * @param requestMessage
     *            The MicrogridsRequestMessage request message to send.
     * @throws ArgumentNullOrEmptyException
     */
    public void send(final MicrogridsRequestMessage requestMessage) throws ArgumentNullOrEmptyException {
        LOGGER.debug("Sending public lighting request message to the queue");

        if (requestMessage.getMessageType() == null) {
            LOGGER.error("MessageType is null");
            throw new ArgumentNullOrEmptyException("MessageType");
        }
        if (StringUtils.isBlank(requestMessage.getOrganisationIdentification())) {
            LOGGER.error("OrganisationIdentification is blank");
            throw new ArgumentNullOrEmptyException("OrganisationIdentification");
        }
        if (StringUtils.isBlank(requestMessage.getDeviceIdentification())) {
            LOGGER.error("DeviceIdentification is blank");
            throw new ArgumentNullOrEmptyException("DeviceIdentification");
        }
        if (StringUtils.isBlank(requestMessage.getCorrelationUid())) {
            LOGGER.error("CorrelationUid is blank");
            throw new ArgumentNullOrEmptyException("CorrelationUid");
        }

        this.sendMessage(requestMessage);
    }

    /**
     * Method for sending a request message to the microgrids requests queue
     *
     * @param requestMessage
     *            The MicrogridsRequestMessage request message to send.
     */
    private void sendMessage(final MicrogridsRequestMessage requestMessage) {
        LOGGER.info("Sending message to the microgrids requests queue");

        this.microgridsRequestsJmsTemplate.send(new MessageCreator() {

            @Override
            public Message createMessage(final Session session) throws JMSException {
                final ObjectMessage objectMessage = session.createObjectMessage(requestMessage.getRequest());
                objectMessage.setJMSCorrelationID(requestMessage.getCorrelationUid());
                objectMessage.setJMSType(requestMessage.getMessageType().toString());
                objectMessage.setStringProperty(Constants.ORGANISATION_IDENTIFICATION,
                        requestMessage.getOrganisationIdentification());
                objectMessage.setStringProperty(Constants.DEVICE_IDENTIFICATION,
                        requestMessage.getDeviceIdentification());
                return objectMessage;
            }
        });
    }
}
