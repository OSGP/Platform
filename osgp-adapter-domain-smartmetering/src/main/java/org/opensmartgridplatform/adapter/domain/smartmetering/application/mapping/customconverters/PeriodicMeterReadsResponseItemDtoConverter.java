package org.opensmartgridplatform.adapter.domain.smartmetering.application.mapping.customconverters;

import java.util.Date;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.ActiveEnergyValues;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.AmrProfileStatusCode;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.OsgpMeterValue;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PeriodicMeterReads;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.AmrProfileStatusCodeDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DlmsMeterValueDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PeriodicMeterReadsResponseItemDto;

public class PeriodicMeterReadsResponseItemDtoConverter extends
        CustomConverter<PeriodicMeterReadsResponseItemDto, PeriodicMeterReads> {

    private final MapperFactory mapperFactory;

    public PeriodicMeterReadsResponseItemDtoConverter(final MapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    @Override
    public PeriodicMeterReads convert(final PeriodicMeterReadsResponseItemDto source,
            final Type<? extends PeriodicMeterReads> destinationType, final MappingContext mappingContext) {
        Date logTime = source.getLogTime();
        ActiveEnergyValues activeEnergyValues = new ActiveEnergyValues(
                toOsgpMeterValue(source.getActiveEnergyImport()),
                toOsgpMeterValue(source.getActiveEnergyExport()),
                toOsgpMeterValue(source.getActiveEnergyImportTariffOne()),
                toOsgpMeterValue(source.getActiveEnergyImportTariffTwo()),
                toOsgpMeterValue(source.getActiveEnergyExportTariffOne()),
                toOsgpMeterValue(source.getActiveEnergyExportTariffTwo()));
        AmrProfileStatusCode amrProfileStatusCode = toAmrProfileStatusCode(source.getAmrProfileStatusCode());
        return new PeriodicMeterReads(logTime, activeEnergyValues, amrProfileStatusCode);
    }

    private OsgpMeterValue toOsgpMeterValue(DlmsMeterValueDto source) {
        return mapperFactory.getMapperFacade().map(source, OsgpMeterValue.class);
    }

    private AmrProfileStatusCode toAmrProfileStatusCode(AmrProfileStatusCodeDto source) {
        return mapperFactory.getMapperFacade().map(source, AmrProfileStatusCode.class);
    }
}
