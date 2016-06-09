package com.alliander.osgp.domain.microgrids;

import java.math.BigInteger;

import org.joda.time.DateTime;

public class Measurement extends MeasurementIdentifier {
    private BigInteger qualifier;
    private DateTime time;
    private double value;

    public Measurement(final int id, final String node, final BigInteger qualifier, final DateTime time,
            final double value) {
        super(id, node);
        this.qualifier = qualifier;
        this.time = time;
        this.value = value;
    }

    public BigInteger getQualifier() {
        return this.qualifier;
    }

    public DateTime getTime() {
        return this.time;
    }

    public double getValue() {
        return this.value;
    }
}
