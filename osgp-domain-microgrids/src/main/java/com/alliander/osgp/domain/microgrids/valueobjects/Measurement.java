package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;

import org.joda.time.DateTime;

public class Measurement extends MeasurementIdentifier implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3315719218127525093L;

    private int qualifier;
    private DateTime time;
    private double value;

    public Measurement(final int id, final String node, final int qualifier, final DateTime time, final double value) {
        super(id, node);
        this.qualifier = qualifier;
        this.time = time;
        this.value = value;
    }

    public int getQualifier() {
        return this.qualifier;
    }

    public DateTime getTime() {
        return this.time;
    }

    public double getValue() {
        return this.value;
    }
}
