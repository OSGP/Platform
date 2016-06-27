package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;

import org.joda.time.DateTime;

public class SetPoint implements Serializable {

    private static final long serialVersionUID = -8781688280636819412L;

    private String node;
    private double value;
    private DateTime startTime;
    private DateTime endTime;

    public SetPoint(final String node, final double value, final DateTime startTime, final DateTime endTime) {
        super();
        this.node = node;
        this.value = value;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNode() {
        return this.node;
    }

    public double getValue() {
        return this.value;
    }

    public DateTime getStartTime() {
        return this.startTime;
    }

    public DateTime getEndTime() {
        return this.endTime;
    }
}
