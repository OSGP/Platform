package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataResponse implements Serializable {

    private static final long serialVersionUID = 7345936024521675762L;

    private List<MeasurementResultSystemIdentifier> measurementResultSystemIdentifiers;

    public DataResponse(final List<MeasurementResultSystemIdentifier> measurementResultSystemIdentifiers) {
        this.measurementResultSystemIdentifiers = new ArrayList<MeasurementResultSystemIdentifier>(
                measurementResultSystemIdentifiers);
    }

    public List<MeasurementResultSystemIdentifier> getMeasurementResultSystemIdentifiers() {
        return new ArrayList<>(this.measurementResultSystemIdentifiers);
    }

}
