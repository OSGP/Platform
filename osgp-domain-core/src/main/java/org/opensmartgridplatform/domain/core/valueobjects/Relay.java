package org.opensmartgridplatform.domain.core.valueobjects;

public class Relay {
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
