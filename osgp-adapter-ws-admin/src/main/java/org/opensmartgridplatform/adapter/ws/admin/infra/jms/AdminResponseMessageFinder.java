/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.admin.infra.jms;

import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import org.opensmartgridplatform.shared.infra.jms.BaseResponseMessageFinder;

/**
 * Class for retrieving response messages from the admin responses queue by
 * correlation UID.
 */
public final class AdminResponseMessageFinder extends BaseResponseMessageFinder {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminResponseMessageFinder.class);

    /**
     * Autowired JMS template for OSGP domain admin responses queue.
     */
    @Autowired
    @Qualifier("wsAdminIncomingResponsesJmsTemplate")
    private JmsTemplate adminResponsesJmsTemplate;

    @Override
    protected ObjectMessage receiveObjectMessage(final String correlationUid) {
        LOGGER.info("Trying to find message with correlationUID: {}", correlationUid);

        return (ObjectMessage) this.adminResponsesJmsTemplate.receiveSelected(this.getJmsCorrelationId(correlationUid));
    }

}
