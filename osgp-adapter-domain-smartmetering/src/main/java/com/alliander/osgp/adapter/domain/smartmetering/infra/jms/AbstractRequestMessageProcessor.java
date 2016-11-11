package com.alliander.osgp.adapter.domain.smartmetering.infra.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.exceptionhandling.TechnicalException;
import com.alliander.osgp.shared.infra.jms.DeviceMessageMetadata;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessageResultType;

public abstract class AbstractRequestMessageProcessor {

    private static final String ERROR_MSG_UNSUPPORTED_OPERATION = "Operation %s is not supported, it must be overridden by the implementing class.";

    @Autowired
    protected WebServiceResponseMessageSender webServiceResponseMessageSender;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequestMessageProcessor.class);

    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata, final Object dataObject)
            throws FunctionalException {
        throw new UnsupportedOperationException(String.format(ERROR_MSG_UNSUPPORTED_OPERATION,
                "handleMessage(deviceMessageMetadata, dataObject)"));
    }

    protected void handleMessage(final DeviceMessageMetadata deviceMessageMetadata) throws FunctionalException {
        throw new UnsupportedOperationException(String.format(ERROR_MSG_UNSUPPORTED_OPERATION,
                "handleMessage(deviceMessageMetadata)"));
    }

    /**
     * In case of an error, this function can be used to send a response
     * containing the exception to the web-service-adapter.
     *
     * @param e
     *            The exception
     * @param deviceMessageMetadata
     *            The {@link DeviceMessageMetadata}
     */
    protected void handleError(final Exception e, final DeviceMessageMetadata deviceMessageMetadata,
            final String errorMessage) {
        LOGGER.info("handeling error: {} for message type: {}", e.getMessage(), deviceMessageMetadata.getMessageType());
        final OsgpException osgpException = this.ensureOsgpException(e, errorMessage);

        this.webServiceResponseMessageSender.send(new ResponseMessage(deviceMessageMetadata.getCorrelationUid(),
                deviceMessageMetadata.getOrganisationIdentification(), deviceMessageMetadata.getDeviceIdentification(),
                ResponseMessageResultType.NOT_OK, osgpException, null, deviceMessageMetadata.getMessagePriority()),
                deviceMessageMetadata.getMessageType());
    }

    protected OsgpException ensureOsgpException(final Exception e, final String errorMessage) {

        if (e instanceof OsgpException) {
            return (OsgpException) e;
        }

        return new TechnicalException(ComponentType.DOMAIN_SMART_METERING, errorMessage, e);
    }

}
