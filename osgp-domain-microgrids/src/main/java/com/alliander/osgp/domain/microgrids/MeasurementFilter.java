package com.alliander.osgp.domain.microgrids;

import java.io.Serializable;

public class MeasurementFilter extends MeasurementIdentifier implements Serializable {

    private static final long serialVersionUID = -5169545289993816729L;

    private boolean all;

    public MeasurementFilter(final int id, final String node, final boolean all) {
        super(id, node);
        this.all = all;
    }

    public boolean isAll() {
        return this.all;
    }

}
