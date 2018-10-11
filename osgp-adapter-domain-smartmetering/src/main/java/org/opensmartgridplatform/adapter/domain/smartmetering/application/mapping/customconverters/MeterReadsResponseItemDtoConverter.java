package org.opensmartgridplatform.adapter.domain.smartmetering.application.mapping.customconverters;

import java.util.Date;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ActiveEnergyValues;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.AmrProfileStatusCode;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.MeterReads;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.OsgpMeterValue;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReads;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AmrProfileStatusCodeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DlmsMeterValueDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.MeterReadsResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsResponseItemDto;

public class MeterReadsResponseItemDtoConverter extends
        CustomConverter<MeterReadsResponseDto, MeterReads> {

    private final MapperFactory mapperFactory;

    public MeterReadsResponseItemDtoConverter(final MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    @Override
    public MeterReads convert(final MeterReadsResponseDto source, final Type<? extends MeterReads> destinationType,
            final MappingContext mappingContext) {
        final Date logTime = source.getLogTime();
        final ActiveEnergyValues activeEnergyValues = new ActiveEnergyValues(
                toOsgpMeterValue(source.getActiveEnergyImport()),
                toOsgpMeterValue(source.getActiveEnergyExport()),
                toOsgpMeterValue(source.getActiveEnergyImportTariffOne()),
                toOsgpMeterValue(source.getActiveEnergyImportTariffTwo()),
                toOsgpMeterValue(source.getActiveEnergyExportTariffOne()),
                toOsgpMeterValue(source.getActiveEnergyExportTariffTwo()));
        return new MeterReads(logTime, activeEnergyValues);
    }

    private OsgpMeterValue toOsgpMeterValue(DlmsMeterValueDto source) {
        return mapperFactory.getMapperFacade().map(source, OsgpMeterValue.class);
    }
}
