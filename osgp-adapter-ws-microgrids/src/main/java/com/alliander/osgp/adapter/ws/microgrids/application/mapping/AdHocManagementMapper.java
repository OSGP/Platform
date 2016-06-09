package com.alliander.osgp.adapter.ws.microgrids.application.mapping;

import org.springframework.stereotype.Component;

import com.alliander.osgp.domain.microgrids.DataRequest;
import com.alliander.osgp.domain.microgrids.DataResponse;
import com.alliander.osgp.domain.microgrids.MeasurementResultSystemIdentifier;
import com.alliander.osgp.domain.microgrids.SystemFilter;
import com.alliander.osgp.shared.mappers.XMLGregorianCalendarToDateTimeConverter;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component(value = "microgridsAdhocManagementMapper")
public class AdHocManagementMapper extends ConfigurableMapper {
    @Override
    public void configure(final MapperFactory mapperFactory) {

        mapperFactory.classMap(com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.SystemFilter.class,
                SystemFilter.class).field("measurementFilter", "measurementFilters").byDefault().register();

        mapperFactory.classMap(com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataRequest.class,
                DataRequest.class).field("systemFilter", "systemFilters").byDefault().register();

        mapperFactory
                .classMap(MeasurementResultSystemIdentifier.class,
                        com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.MeasurementResultSystemIdentifier.class)
                .field("measurements", "measdurement").byDefault().register();

        mapperFactory
                .classMap(DataResponse.class,
                        com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.GetDataResponse.class)
                .field("measurementResultSystemIdentifiers", "system").byDefault().register();

        mapperFactory.getConverterFactory().registerConverter(new XMLGregorianCalendarToDateTimeConverter());
    }
}
