package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemFilter extends SystemIdentifier {
    private List<MeasurementFilter> measurementFilters;
    private boolean all;

    public SystemFilter(final int id, final String type, final List<MeasurementFilter> measurementFilters,
            final boolean all) {
        super(id, type);
        this.measurementFilters = new ArrayList<MeasurementFilter>(measurementFilters);
        this.all = all;
    }

    public List<MeasurementFilter> getMeasurementFilters() {
        return Collections.unmodifiableList(this.measurementFilters);
    }

    public boolean isAll() {
        return this.all;
    }
}
