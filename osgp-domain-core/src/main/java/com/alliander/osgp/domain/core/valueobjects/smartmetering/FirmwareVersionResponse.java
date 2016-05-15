/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.util.List;

public class FirmwareVersionResponse extends ActionResponse {

    private static final long serialVersionUID = 4471818892560224779L;

    private List<FirmwareVersion> firmwareVersions;

    public FirmwareVersionResponse(final List<FirmwareVersion> firmwareVersions) {
        super();
        this.firmwareVersions = firmwareVersions;
    }

    public List<FirmwareVersion> getFirmwareVersions() {
        return this.firmwareVersions;
    }

}
