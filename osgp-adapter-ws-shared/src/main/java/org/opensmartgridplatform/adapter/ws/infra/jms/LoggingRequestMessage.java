/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.infra.jms;

import java.util.Date;

import org.opensmartgridplatform.shared.domain.CorrelationIds;

public class LoggingRequestMessage {

    private final Date timeStamp;
    private final CorrelationIds ids;
    private final String userName;
    private final String applicationName;
    private final String className;
    private final String methodName;
    private String responseResult;
    private final int resposeDataSize;

    // Logging items.
    public LoggingRequestMessage(final Date timeStamp, final CorrelationIds ids, final String userName,
            final String applicationName, final String className, final String methodName, final String responseResult,
            final int resposeDataSize) {
        this.ids = ids;
        this.timeStamp = (Date) timeStamp.clone();
        this.userName = userName;
        this.applicationName = applicationName;
        this.className = className;
        this.methodName = methodName;
        this.responseResult = responseResult;
        this.resposeDataSize = resposeDataSize;
    }

    public Date getTimeStamp() {
        return (Date) this.timeStamp.clone();
    }

    public String getOrganisationIdentification() {
        return this.ids.getOrganisationIdentification();
    }

    public String getUserName() {
        return this.userName;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getDeviceIdentification() {
        return this.ids.getDeviceIdentification();
    }

    public String getCorrelationUid() {
        return this.ids.getCorrelationUid();
    }

    public String getResponseResult() {
        return this.responseResult;
    }

    public int getResposeDataSize() {
        return this.resposeDataSize;
    }

    public void setResponseResult(final String responseResult) {
        this.responseResult = responseResult;
    }
}
