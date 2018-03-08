/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.alliander.osgp.shared.domain.entities.AbstractEntity;
import com.alliander.osgp.shared.infra.db.DefaultConnectionPoolFactory;
import com.alliander.osgp.shared.infra.jms.ResponseMessageResultType;

@Entity
@Table(name = "response_data")
public class ResponseData extends AbstractEntity {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 493031146792643786L;

    @Column(length = 255)
    private String organisationIdentification;

    @Column(length = 255)
    private String messageType;

    @Column(length = 255)
    private String deviceIdentification;

    @Column(length = 255)
    private String correlationUid;

    @Column
    private Short numberOfNotificationsSent;

    @Column(length = 255)
    private String responseUrl;

    @Enumerated(EnumType.STRING)
    private ResponseMessageResultType resultType;

    @Type(type = "java.io.Serializable")
    private Serializable messageData;

    private ResponseData() {

    }

    public String getCorrelationUid() {
        return this.correlationUid;
    }

    public String getOrganisationIdentification() {
        return this.organisationIdentification;
    }

    public String getDeviceIdentification() {
        return this.deviceIdentification;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public Serializable getMessageData() {
        return this.messageData;
    }

    public ResponseMessageResultType getResultType() {
        return this.resultType;
    }

    public Short getNumberOfNotificationsSent() {
        return this.numberOfNotificationsSent;
    }

    public void setNumberOfNotificationsSent(final Short numberOfNotificationsSent) {
        this.numberOfNotificationsSent = numberOfNotificationsSent;
    }

    public String getResponseUrl() {
        return this.responseUrl;
    }

    public void setResponseUrl(final String responseUrl) {
        this.responseUrl = responseUrl;
    }

    /**
     * Builder class which can construct an {@link DefaultConnectionPoolFactory}
     * instance.}
     */
    public static class Builder {
        private String organisationIdentification;
        private String messageType;
        private String deviceIdentification;
        private String correlationUid;
        private Short numberOfNotificationsSent = (short) 0;
        private String responseUrl;
        private ResponseMessageResultType resultType;
        private Serializable messageData;

        public Builder withOrganisationIdentification(final String organisationIdentification) {
            this.organisationIdentification = organisationIdentification;
            return this;
        }

        public Builder withMessageType(final String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder withDeviceIdentification(final String deviceIdentification) {
            this.deviceIdentification = deviceIdentification;
            return this;
        }

        public Builder withCorrelationUid(final String correlationUid) {
            this.correlationUid = correlationUid;
            return this;
        }

        public Builder withNumberOfNotificationsSent(final Short numberOfNotificationsSent) {
            this.numberOfNotificationsSent = numberOfNotificationsSent;
            return this;
        }

        public Builder withResponseUrl(final String responseUrl) {
            this.responseUrl = responseUrl;
            return this;
        }

        public Builder withResultType(final ResponseMessageResultType resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder withMessageData(final Serializable messageData) {
            this.messageData = messageData;
            return this;
        }

        public ResponseData build() {
            final ResponseData responseData = new ResponseData();
            responseData.organisationIdentification = this.organisationIdentification;
            responseData.messageType = this.messageType;
            responseData.deviceIdentification = this.deviceIdentification;
            responseData.correlationUid = this.correlationUid;
            responseData.numberOfNotificationsSent = this.numberOfNotificationsSent;
            responseData.responseUrl = this.responseUrl;
            responseData.resultType = this.resultType;
            responseData.messageData = this.messageData;

            return responseData;
        }
    }

}
