package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataResponse {

    private List<MeasurementResultSystemIdentifier> measurementResultSystemIdentifiers;

    public DataResponse(final List<MeasurementResultSystemIdentifier> measurementResultSystemIdentifiers) {
        this.measurementResultSystemIdentifiers = new ArrayList<MeasurementResultSystemIdentifier>(
                measurementResultSystemIdentifiers);
    }

    public List<MeasurementResultSystemIdentifier> getMeasurementResultSystemIdentifiers() {
        return Collections.unmodifiableList(this.measurementResultSystemIdentifiers);
    }

}
