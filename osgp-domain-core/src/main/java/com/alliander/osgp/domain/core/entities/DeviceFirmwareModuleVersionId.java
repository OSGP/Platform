package com.alliander.osgp.domain.core.entities;

import java.io.Serializable;

public class DeviceFirmwareModuleVersionId implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -33634025846357516L;

    private Long deviceId;
    private String moduleDescription;
    private String moduleVersion;

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(final Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getModuleDescription() {
        return this.moduleDescription;
    }

    public void setModuleDescription(final String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String getModuleVersion() {
        return this.moduleVersion;
    }

    public void setModuleVersion(final String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }
}
