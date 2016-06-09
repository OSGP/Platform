package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataRequest {

    private List<SystemFilter> systemFilters;

    public DataRequest(final List<SystemFilter> systemFilters) {
        this.systemFilters = new ArrayList<SystemFilter>(systemFilters);
    }

    public List<SystemFilter> getSystemFilters() {
        return Collections.unmodifiableList(this.systemFilters);
    }
}
