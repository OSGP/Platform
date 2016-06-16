package com.alliander.osgp.domain.microgrids.valueobjects;

import java.io.Serializable;

public class SystemIdentifier implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3313598698244220718L;

    private int id;
    private String systemType;

    public SystemIdentifier(final int id, final String systemType) {
        this.id = id;
        this.systemType = systemType;
    }

    public int getId() {
        return this.id;
    }

    public String getSystemType() {
        return this.systemType;
    }
}
