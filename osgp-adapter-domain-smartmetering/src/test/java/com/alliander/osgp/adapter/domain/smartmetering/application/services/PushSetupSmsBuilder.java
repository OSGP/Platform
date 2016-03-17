/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import java.util.ArrayList;
import java.util.List;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.ClockStatus;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemDate;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemDateTime;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObisCode;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemObjectDefinition;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.CosemTime;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.MessageType;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PushSetupSms;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SendDestinationAndMethod;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.TransportServiceType;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.WindowElement;

/**
 * @author dev
 *
 */
public class PushSetupSmsBuilder {
    private PushSetupSms pushSetupSms;

    public PushSetupSmsBuilder withNullValues() {
        this.pushSetupSms = new PushSetupSms.Builder().build();
        return this;
    }

    public PushSetupSmsBuilder withEmptyLists() {
        final CosemObisCode logicalName = new CosemObisCode(1, 2, 3, 4, 5, 6);
        final TransportServiceType transportServiceType = TransportServiceType.TCP;
        final MessageType messageType = MessageType.A_XDR_ENCODED_X_DLMS_APDU;
        final SendDestinationAndMethod sendDestinationAndMethod = new SendDestinationAndMethod(transportServiceType,
                "destination", messageType);

        final Integer randomisationStartInterval = new Integer(1);
        final Integer numberOfRetries = new Integer(10);
        final Integer repetitionDelay = new Integer(2);

        // Empty Lists
        final List<CosemObjectDefinition> pushObjectList = new ArrayList<>();
        final List<WindowElement> communicationWindow = new ArrayList<>();

        this.pushSetupSms = new PushSetupSms.Builder().logicalName(logicalName).pushObjectList(pushObjectList)
                .sendDestinationAndMethod(sendDestinationAndMethod).communicationWindow(communicationWindow)
                .randomisationStartInterval(randomisationStartInterval).numberOfRetries(numberOfRetries)
                .repetitionDelay(repetitionDelay).build();
        return this;
    }

    public PushSetupSmsBuilder withLists() {
        final CosemObisCode logicalName = new CosemObisCode(1, 2, 3, 4, 5, 6);
        final TransportServiceType transportServiceType = TransportServiceType.TCP;
        final MessageType messageType = MessageType.A_XDR_ENCODED_X_DLMS_APDU;
        final SendDestinationAndMethod sendDestinationAndMethod = new SendDestinationAndMethod(transportServiceType,
                "destination", messageType);

        final Integer randomisationStartInterval = new Integer(1);
        final Integer numberOfRetries = new Integer(10);
        final Integer repetitionDelay = new Integer(2);

        // Lists with one entry
        final CosemObjectDefinition cosemObjectDefinition = new CosemObjectDefinition(1, logicalName, 2);
        final List<CosemObjectDefinition> pushObjectList = new ArrayList<>();
        pushObjectList.add(cosemObjectDefinition);

        final CosemDateTime startTime = new CosemDateTime(new CosemDate(2016, 3, 17), new CosemTime(11, 52, 45), 0,
                new ClockStatus(ClockStatus.STATUS_NOT_SPECIFIED));
        final CosemDateTime endTime = new CosemDateTime(new CosemDate(2016, 3, 17), new CosemTime(11, 52, 45), 0,
                new ClockStatus(ClockStatus.STATUS_NOT_SPECIFIED));
        final WindowElement windowElement = new WindowElement(startTime, endTime);
        final List<WindowElement> communicationWindow = new ArrayList<>();
        communicationWindow.add(windowElement);

        this.pushSetupSms = new PushSetupSms.Builder().logicalName(logicalName).pushObjectList(pushObjectList)
                .sendDestinationAndMethod(sendDestinationAndMethod).communicationWindow(communicationWindow)
                .randomisationStartInterval(randomisationStartInterval).numberOfRetries(numberOfRetries)
                .repetitionDelay(repetitionDelay).build();
        return this;
    }

    public PushSetupSms build() {
        return this.pushSetupSms;
    }
}
