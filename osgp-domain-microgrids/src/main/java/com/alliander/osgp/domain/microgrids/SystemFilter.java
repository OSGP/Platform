package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemFilter extends SystemIdentifier {
    private List<MeasurementFilter> measurementFilter;
    private boolean all;

    public SystemFilter(final int id, final String type, final List<MeasurementFilter> measurementFilter,
            final boolean all) {
        super(id, type);
        this.measurementFilter = new ArrayList<MeasurementFilter>(measurementFilter);
        this.all = all;
    }

    public List<MeasurementFilter> getMeasurementFilter() {
        return Collections.unmodifiableList(this.measurementFilter);
    }

    public boolean isAll() {
        return this.all;
    }
}
