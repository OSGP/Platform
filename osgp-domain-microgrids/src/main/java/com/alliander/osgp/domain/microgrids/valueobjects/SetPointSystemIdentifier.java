package com.alliander.osgp.domain.microgrids.valueobjects;

public class SetPointSystemIdentifier extends SystemIdentifier {

    private static final long serialVersionUID = 9130054367163068097L;

    private SetPoint setPoint;

    public SetPointSystemIdentifier(final int id, final String systemType, final SetPoint setPoint) {
        super(id, systemType);
        this.setPoint = setPoint;
    }

    public SetPoint getSetPoint() {
        return this.setPoint;
    }
}
