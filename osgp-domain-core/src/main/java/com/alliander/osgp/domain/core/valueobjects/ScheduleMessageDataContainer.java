/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects;

import java.io.Serializable;
import java.util.List;

public class ScheduleMessageDataContainer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 280691205768966372L;

    private final Short astronomicalSunriseOffset;
    private final Short astronomicalSunsetOffset;

    private final List<Schedule> scheduleList;

    public ScheduleMessageDataContainer(final List<Schedule> scheduleList) {
        this(scheduleList, null, null);
    }

    public ScheduleMessageDataContainer(final List<Schedule> scheduleList, final Short astronomicalSunriseOffset,
            final Short astronomicalSunsetOffset) {
        this.astronomicalSunriseOffset = astronomicalSunriseOffset;
        this.astronomicalSunsetOffset = astronomicalSunsetOffset;
        this.scheduleList = scheduleList;
    }

    public Short getAstronomicalSunriseOffset() {
        return this.astronomicalSunriseOffset;
    }

    public Short getAstronomicalSunsetOffset() {
        return this.astronomicalSunsetOffset;
    }

    public List<Schedule> getScheduleList() {
        return this.scheduleList;
    }
}
