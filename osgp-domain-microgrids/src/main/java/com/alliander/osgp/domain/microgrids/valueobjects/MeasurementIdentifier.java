package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;

public class MeasurementIdentifier implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3933967809366650885L;

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
