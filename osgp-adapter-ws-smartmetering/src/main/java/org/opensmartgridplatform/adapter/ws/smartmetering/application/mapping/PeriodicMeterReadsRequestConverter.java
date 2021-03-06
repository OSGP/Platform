/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.ws.smartmetering.application.mapping;

import org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasRequest;
import org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.PeriodicReadsRequest;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReadsQuery;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class PeriodicMeterReadsRequestConverter extends
        CustomConverter<org.opensmartgridplatform.adapter.ws.schema.smartmetering.monitoring.PeriodicReadsRequest, PeriodicMeterReadsQuery> {

    @Override
    public boolean canConvert(final Type<?> sourceType, final Type<?> destinationType) {
        return this.sourceType.isAssignableFrom(sourceType) && this.destinationType.equals(destinationType);
    }

    @Override
    public PeriodicMeterReadsQuery convert(final PeriodicReadsRequest source,
            final Type<? extends PeriodicMeterReadsQuery> destinationType, final MappingContext context) {
        return new PeriodicMeterReadsQuery(
                org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodType
                        .valueOf(source.getPeriodicReadsRequestData().getPeriodType().name()),
                source.getPeriodicReadsRequestData().getBeginDate().toGregorianCalendar().getTime(),
                source.getPeriodicReadsRequestData().getEndDate().toGregorianCalendar().getTime(),
                source instanceof PeriodicMeterReadsGasRequest, "");
    }

}
