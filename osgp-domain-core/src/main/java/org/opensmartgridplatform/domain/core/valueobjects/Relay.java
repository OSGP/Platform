package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;

public class Relay implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 6780427560822971719L;
    private final int actualCurrent;
    private final int actualPower;
    private final int averagePowerFactor;

    public Relay(final int actualCurrent, final int actualPower, final int averagePowerFactor) {
        this.actualCurrent = actualCurrent;
        this.actualPower = actualPower;
        this.averagePowerFactor = averagePowerFactor;
    }

    public int getActualCurrent() {
        return actualCurrent;
    }

    public int getActualPower() {
        return actualPower;
    }

    public int getAveragePowerFactor() {
        return averagePowerFactor;
    }
}
