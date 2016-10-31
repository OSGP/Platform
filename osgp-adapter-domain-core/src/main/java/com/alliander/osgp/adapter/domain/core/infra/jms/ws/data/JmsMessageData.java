/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.core.infra.jms.ws.data;

/**
 * contains the common message data objects used by the message processors
 */
public class JmsMessageData {

    private String correlationUid;
    private String messageType;
    private String organisationIdentification;
    private String deviceIdentification;

    public String getCorrelationUid() {
        return this.correlationUid;
    }

    public void setCorrelationUid(final String correlationUid) {
        this.correlationUid = correlationUid;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(final String messageType) {
        this.messageType = messageType;
    }

    public String getOrganisationIdentification() {
        return this.organisationIdentification;
    }

    public void setOrganisationIdentification(final String organisationIdentification) {
        this.organisationIdentification = organisationIdentification;
    }

    public String getDeviceIdentification() {
        return this.deviceIdentification;
    }

    public void setDeviceIdentification(final String deviceIdentification) {
        this.deviceIdentification = deviceIdentification;
    }

}
