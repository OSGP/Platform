package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataRequest implements Serializable {

    private static final long serialVersionUID = 4776483459295815846L;

    private List<SystemFilter> systemFilters;

    public DataRequest(final List<SystemFilter> systemFilters) {
        this.systemFilters = new ArrayList<SystemFilter>(systemFilters);
    }

    public List<SystemFilter> getSystemFilters() {
        return this.systemFilters;
    }
}
