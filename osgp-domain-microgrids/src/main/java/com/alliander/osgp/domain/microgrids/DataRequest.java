package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataRequest {

    private List<SystemFilter> systemFilter;

    public DataRequest(final List<SystemFilter> systemFilter) {
        this.systemFilter = new ArrayList<SystemFilter>(systemFilter);
    }

    public List<SystemFilter> getSystemFilter() {
        return Collections.unmodifiableList(this.systemFilter);
    }
}
