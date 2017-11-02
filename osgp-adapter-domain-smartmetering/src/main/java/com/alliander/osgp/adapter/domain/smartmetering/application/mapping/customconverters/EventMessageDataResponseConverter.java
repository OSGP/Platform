/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.mapping.customconverters;

import java.util.ArrayList;
import java.util.List;

import com.alliander.osgp.domain.core.valueobjects.smartmetering.Event;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.EventMessagesResponse;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.OsgpResultType;
import com.alliander.osgp.dto.valueobjects.smartmetering.EventDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.EventMessageDataResponseDto;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class EventMessageDataResponseConverter
        extends CustomConverter<EventMessageDataResponseDto, EventMessagesResponse> {

    @Override
    public EventMessagesResponse convert(final EventMessageDataResponseDto source,
            final Type<? extends EventMessagesResponse> destination, final MappingContext context) {
        if (source != null) {
            final List<EventDto> events = source.getEvents();

            final List<Event> convertedEvents = new ArrayList<>();
            if (events != null) {
                for (final EventDto event : events) {
                    final Event convertedEvent = new Event(event.getTimestamp(), event.getEventCode(),
                            event.getEventCounter());
                    convertedEvents.add(convertedEvent);
                }

                return new EventMessagesResponse(convertedEvents, OsgpResultType.valueOf(source.getResult().name()),
                        source.getException(), source.getResultString());
            } else {
                return new EventMessagesResponse(null, OsgpResultType.valueOf(source.getResult().name()),
                        source.getException(), source.getResultString());
            }
        }
        return null;
    }
}
