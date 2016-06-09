package com.alliander.osgp.adapter.ws.microgrids.application.services;

import javax.validation.constraints.NotNull;

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
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.domain.microgrids.DataRequest;
import com.alliander.osgp.domain.microgrids.DataResponse;
import com.alliander.osgp.domain.microgrids.entities.RtuDevice;
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

    public MicrogridsService() {
        // Parameterless constructor required for transactions
    }

    public String enqueueGetDataRequest(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, @NotNull final DataRequest dataRequest)
            throws FunctionalException {

        final Organisation organisation = this.domainHelperService.findOrganisation(organisationIdentification);
        final RtuDevice device = this.domainHelperService.findDevice(deviceIdentification);

        this.domainHelperService.isAllowed(organisation, device, DeviceFunction.GET_DATA);

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

        return correlationUid;
    }

    public DataResponse dequeueGetDataResponse(final String correlationUid) {
        return null;
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
