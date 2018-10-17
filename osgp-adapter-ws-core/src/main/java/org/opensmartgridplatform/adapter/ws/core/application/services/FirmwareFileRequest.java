package org.opensmartgridplatform.adapter.ws.core.application.services;

public class FirmwareFileRequest {
    private final String description;
    private final String fileName;
    private final boolean pushToNewDevices;

    public FirmwareFileRequest(final String description, final String fileName, final boolean pushToNewDevices) {
        this.description = description;
        this.fileName = fileName;
        this.pushToNewDevices = pushToNewDevices;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPushToNewDevices() {
        return pushToNewDevices;
    }
}
