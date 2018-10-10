/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects;

import java.util.ArrayList;
import java.util.List;

public class SsldData implements java.io.Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 4785387346353649099L;

    private final Relay relay1;
    private final Relay relay2;
    private final Relay relay3;
    private final List<RelayData> relayData;

    public SsldData(final Relay relay1, final Relay relay2, final Relay relay3,
            final List<RelayData> relayData) {
        this.relay1 = relay1;
        this.relay2 = relay2;
        this.relay3 = relay3;
        this.relayData = relayData;
    }

    public int getActualCurrent1() {
        return this.relay1.getActualCurrent();
    }

    public int getActualCurrent2() {
        return this.relay2.getActualCurrent();
    }

    public int getActualCurrent3() {
        return this.relay3.getActualCurrent();
    }

    public int getActualPower1() {
        return this.relay1.getActualPower();
    }

    public int getActualPower2() {
        return this.relay2.getActualPower();
    }

    public int getActualPower3() {
        return this.relay3.getActualPower();
    }

    public int getAveragePowerFactor1() {
        return this.relay1.getAveragePowerFactor();
    }

    public int getAveragePowerFactor2() {
        return this.relay2.getAveragePowerFactor();
    }

    public int getAveragePowerFactor3() {
        return this.relay3.getAveragePowerFactor();
    }

    public List<RelayData> getRelayData() {
        return this.relayData;
    }
}

