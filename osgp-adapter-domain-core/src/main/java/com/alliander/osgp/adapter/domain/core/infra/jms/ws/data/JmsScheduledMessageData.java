/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.core.infra.jms.ws.data;

/**
 * extends the common message data objects with scheduling properties
 */
public class JmsScheduledMessageData extends JmsMessageData {

    Boolean isScheduled;
    Long scheduleTime;

    public Boolean getIsScheduled() {
        return this.isScheduled;
    }

    public void setIsScheduled(final Boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public Long getScheduleTime() {
        return this.scheduleTime;
    }

    public void setScheduleTime(final Long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

}
