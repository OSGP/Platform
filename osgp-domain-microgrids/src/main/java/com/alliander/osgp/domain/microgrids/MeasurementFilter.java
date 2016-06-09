package com.alliander.osgp.domain.microgrids;

public class MeasurementFilter extends MeasurementIdentifier {
    private boolean all;

    public MeasurementFilter(final int id, final String node, final boolean all) {
        super(id, node);
        this.all = all;
    }

    public boolean isAll() {
        return this.all;
    }

}
