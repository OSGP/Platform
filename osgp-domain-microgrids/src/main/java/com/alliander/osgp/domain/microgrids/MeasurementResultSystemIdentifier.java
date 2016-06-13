package com.alliander.osgp.domain.microgrids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeasurementResultSystemIdentifier extends SystemIdentifier {
    private List<Measurement> measurements;

    public MeasurementResultSystemIdentifier(final int id, final String type, final List<Measurement> measurements) {
        super(id, type);
        this.measurements = new ArrayList<Measurement>(measurements);
    }

    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(this.measurements);
    }
}
