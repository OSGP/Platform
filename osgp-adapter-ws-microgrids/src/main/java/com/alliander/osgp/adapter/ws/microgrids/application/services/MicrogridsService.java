package com.alliander.osgp.adapter.ws.microgrids.application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.ws.microgrids.infra.jms.MicrogridsRequestMessageSender;
import com.alliander.osgp.adapter.ws.microgrids.infra.jms.MicrogridsResponseMessageFinder;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.services.CorrelationIdProviderService;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.domain.microgrids.DataRequest;
import com.alliander.osgp.domain.microgrids.DataResponse;
import com.alliander.osgp.domain.microgrids.Measurement;
import com.alliander.osgp.domain.microgrids.MeasurementFilter;
import com.alliander.osgp.domain.microgrids.MeasurementResultSystemIdentifier;
import com.alliander.osgp.domain.microgrids.SystemFilter;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

@Service
@Transactional(value = "transactionManager")
@Validated
public class MicrogridsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MicrogridsService.class);

    @Autowired
    private DomainHelperService domainHelperService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CorrelationIdProviderService correlationIdProviderService;

    @Autowired
    private MicrogridsRequestMessageSender requestMessageSender;

    @Autowired
    private MicrogridsResponseMessageFinder responseMessageFinder;

    private Map<String, DataRequest> mockRequestHolder = new HashMap<>();

    public MicrogridsService() {
        // Parameterless constructor required for transactions
    }

    public String enqueueGetDataRequest(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, @NotNull final DataRequest dataRequest)
            throws FunctionalException {

        final Organisation organisation = this.domainHelperService.findOrganisation(organisationIdentification);
        // TODO disabled for mock responses (for now)
        // final RtuDevice device =
        // this.domainHelperService.findDevice(deviceIdentification);

        // TODO disabled for mock responses (for now)
        // this.domainHelperService.isAllowed(organisation, device,
        // DeviceFunction.GET_DATA);

        LOGGER.debug("enqueueGetDataRequest called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        // TODO
        // final MicrogridsRequestMessage message = new
        // MicrogridsRequestMessage(MicrogridsRequestMessageType.GET_DATA,
        // correlationUid, organisationIdentification, deviceIdentification,
        // dataRequest, null);
        //
        // this.requestMessageSender.send(message);

        this.mockRequestHolder.put(correlationUid, dataRequest);

        return correlationUid;
    }

    public DataResponse dequeueGetDataResponse(final String correlationUid) {
        // Send fake response, depending on requested data
        if (!this.mockRequestHolder.containsKey(correlationUid)) {
            return null;
        }

        final List<MeasurementResultSystemIdentifier> results = new ArrayList<>();
        final DataRequest request = this.mockRequestHolder.get(correlationUid);
        final DateTime sampleTime = new DateTime(DateTimeZone.UTC);

        // Use original filters to fake responses
        for (final SystemFilter systemFilter : request.getSystemFilters()) {
            final List<Measurement> measurements = new ArrayList<>();

            if (systemFilter.isAll()) {
                measurements.add(new Measurement(1, "actualpower", 0, sampleTime, 33.0));
                measurements.add(new Measurement(1, "UL", 0, sampleTime, 44.0));
                measurements.add(new Measurement(2, "UL", 0, sampleTime, 55.0));
                measurements.add(new Measurement(3, "UL", 0, sampleTime, 66.0));
            } else {
                for (final MeasurementFilter measurementFilter : systemFilter.getMeasurementFilters()) {
                    measurements.add(new Measurement(1, measurementFilter.getNode(), 0, sampleTime, 99.0));
                }
            }

            results.add(
                    new MeasurementResultSystemIdentifier(systemFilter.getId(), systemFilter.getType(), measurements));
        }

        this.mockRequestHolder.remove(correlationUid);

        return new DataResponse(results);

        // final DataResponse response = new DataResponse();

        // TODO
        // return this.responseMessageFinder.findMessage(correlationUid);

        // final ResponseMessage message = this.service
        // .dequeueGetStatusResponse(request.getAsyncRequest().getCorrelationUid());
        // if (message != null) {
        // response.setResult(OsgpResultType.fromValue(message.getResult().getValue()));
        //
        // if (message.getDataObject() != null) {
        // final List<MeasurementResultSystemIdentifier> identifiers = new
        // ArrayList<>();
        // if (message.getDataObject() instanceof ArrayList<?>) {
        // for (final Object item : (ArrayList<?>) message.getDataObject()) {
        // if (item instanceof MeasurementResultSystemIdentifier) {
        // identifiers.add((MeasurementResultSystemIdentifier) item);
        // }
        // }
        // }
        // response.getSystem().addAll(this.mapper.mapAsList(identifiers,
        // com.alliander.osgp.adapter.ws.schema.microgrids.adhocmanagement.MeasurementResultSystemIdentifier.class));
        //
        // }
    }

    public String enqueueSetSetPointsRequest(final String organisationIdentification, final String deviceIdentification,
            final Object requestData) {
        // TODO Auto-generated method stub
        return null;
    }

    public ResponseMessage dequeueSetSetPointsResponse(final String correlationUid) {
        // TODO Auto-generated method stub
        return null;
    }

}
