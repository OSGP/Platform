/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.messageprocessors;

import org.opensmartgridplatform.adapter.domain.smartmetering.application.services.ConfigurationService;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.core.OsgpCoreResponseMessageProcessor;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.MessageType;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateAndReplaceKeysResponseMessageProcessor extends OsgpCoreResponseMessageProcessor {

    @Autowired
    private ConfigurationService configurationService;

    protected GenerateAndReplaceKeysResponseMessageProcessor() {
        super(MessageType.GENERATE_AND_REPLACE_KEYS);
    }

    @Override
    protected boolean hasRegularResponseObject(final ResponseMessage responseMessage) {
        // Only the result is used, no need to check the dataObject.
        return true;
    }

    @Override
    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata,
            final ResponseMessage responseMessage, final OsgpException osgpException) {

        this.configurationService.handleGenerateAndReplaceKeysResponse(deviceMessageMetadata, responseMessage.getResult(),
                osgpException);
    }
}
