package com.alliander.osgp.domain.microgrids.valueobjects;

public class MeasurementIdentifier {
    private int id;
    private String node;

    public MeasurementIdentifier(final int id, final String node) {
        super();
        this.id = id;
        this.node = node;
    }

    public int getId() {
        return this.id;
    }

    public String getNode() {
        return this.node;
    }

}
