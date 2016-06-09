package com.alliander.osgp.domain.microgrids;

import java.math.BigInteger;
import java.util.Date;

public class Measurement extends MeasurementIdentifier {
    private BigInteger qualifier;
    private Date time;
l    private double value;

    public Measurement(final int id, final String node, final BigInteger qualifier, final Date time,
            final double value) {
        super(id, node);
        this.qualifier = qualifier;
        this.time = time;
        this.value = value;
    }

    public BigInteger getQualifier() {
        return this.qualifier;
    }

    public Date getTime() {
        return this.time;
    }

    public double getValue() {
        return this.value;
    }
}
