package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {

    private List<MeasurementResultSystemIdentifier> system;

    public DataResponse(final List<MeasurementResultSystemIdentifier> system) {
        this.system = new ArrayList<MeasurementResultSystemIdentifier>(system);
    }

    public List<MeasurementResultSystemIdentifier> getSystem() {
        return this.system;
    }

}
