package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemFilter extends SystemIdentifier implements Serializable {

    private static final long serialVersionUID = 2069822566541617223L;

    private List<MeasurementFilter> measurementFilters;
    private boolean all;

    public SystemFilter(final int id, final String systemType, final List<MeasurementFilter> measurementFilters,
            final boolean all) {
        super(id, systemType);
        this.measurementFilters = new ArrayList<MeasurementFilter>(measurementFilters);
        this.all = all;
    }

    public List<MeasurementFilter> getMeasurementFilters() {
        return this.measurementFilters;
    }

    public boolean isAll() {
        return this.all;
    }
}
