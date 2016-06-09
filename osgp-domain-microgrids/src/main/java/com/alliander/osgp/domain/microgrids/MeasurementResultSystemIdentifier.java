package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeasurementResultSystemIdentifier {
    private List<Measurement> measurements;

    public MeasurementResultSystemIdentifier(final List<Measurement> measurement) {
        this.measurements = new ArrayList<Measurement>(measurement);
    }

    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(this.measurements);
    }

}
