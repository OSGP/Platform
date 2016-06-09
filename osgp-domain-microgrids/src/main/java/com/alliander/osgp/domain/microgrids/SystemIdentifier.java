package com.alliander.osgp.domain.microgrids;

public class SystemIdentifier {
    private int id;
    private String type;

    public SystemIdentifier(final int id, final String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }
}
