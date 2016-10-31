/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.core.infra.jms.ws.data;

/**
 * extends the common message data objects with resultObject
 */
public class JmsObjectMessageData extends JmsMessageData {

    private String resultObject;

    public String getResultObject() {
        return this.resultObject;
    }

    public void setResultObject(final String resultObject) {
        this.resultObject = resultObject;
    }

}
