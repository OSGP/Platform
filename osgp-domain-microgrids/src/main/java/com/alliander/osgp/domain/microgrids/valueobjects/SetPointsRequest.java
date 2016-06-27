package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;
import java.util.List;

public class SetPointsRequest implements Serializable {

    private static final long serialVersionUID = -6528597730317108512L;

    private List<SetPointSystemIdentifier> setPointSystemIdentifiers;

    public SetPointsRequest(final List<SetPointSystemIdentifier> setPointSystemIdentifiers) {
        super();
        this.setPointSystemIdentifiers = setPointSystemIdentifiers;
    }

    public List<SetPointSystemIdentifier> getSetPointSystemIdentifiers() {
        return this.setPointSystemIdentifiers;
    }
}
