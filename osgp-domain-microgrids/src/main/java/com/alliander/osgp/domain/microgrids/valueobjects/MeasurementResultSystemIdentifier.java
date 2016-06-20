package com.alliander.osgp.domain.microgrids.valueobjects;

import java.util.ArrayList;
import java.util.List;

public class MeasurementResultSystemIdentifier extends SystemIdentifier {

    private static final long serialVersionUID = -6201476739756810987L;

    private List<Measurement> measurements;

    public MeasurementResultSystemIdentifier(final int id, final String systemType,
            final List<Measurement> measurements) {
        super(id, systemType);
        this.measurements = new ArrayList<Measurement>(measurements);
    }

    public List<Measurement> getMeasurements() {
        return new ArrayList<>(this.measurements);
    }
}
