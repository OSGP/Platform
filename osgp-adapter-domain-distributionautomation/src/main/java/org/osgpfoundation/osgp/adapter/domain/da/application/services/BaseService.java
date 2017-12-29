/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.domain.da.application.services;

import java.util.UUID;

import javax.persistence.OptimisticLockException;

import org.joda.time.DateTime;
import org.osgpfoundation.osgp.adapter.domain.da.application.mapping.DomainDistributionAutomationMapper;
import org.osgpfoundation.osgp.adapter.domain.da.infra.jms.core.OsgpCoreRequestMessageSender;
import org.osgpfoundation.osgp.adapter.domain.da.infra.jms.ws.WebServiceResponseMessageSender;
import org.osgpfoundation.osgp.domain.da.entities.RtuDevice;
import org.osgpfoundation.osgp.domain.da.repositories.RtuDeviceRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.exceptions.UnknownEntityException;
import com.alliander.osgp.domain.core.services.DeviceDomainService;
import com.alliander.osgp.domain.core.services.OrganisationDomainService;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.exceptionhandling.TechnicalException;

public class BaseService {

    @Autowired
    protected DeviceDomainService deviceDomainService;

    @Autowired
    protected OrganisationDomainService organisationDomainService;

    @Autowired
    protected RtuDeviceRepository rtuDeviceRepository;

    @Autowired
    @Qualifier(value = "domainDistributionAutomationOutgoingOsgpCoreRequestMessageSender")
    protected OsgpCoreRequestMessageSender osgpCoreRequestMessageSender;

    @Autowired
    protected DomainDistributionAutomationMapper domainCoreMapper;

    @Autowired
    @Qualifier(value = "domainDistributionAutomationOutgoingWebServiceResponseMessageSender")
    protected WebServiceResponseMessageSender webServiceResponseMessageSender;

    @Autowired
    private Integer lastCommunicationUpdateInterval;

    protected Device findActiveDevice(final String deviceIdentification) throws FunctionalException {
        return this.deviceDomainService.searchActiveDevice(deviceIdentification,
                ComponentType.DOMAIN_DISTRIBUTION_AUTOMATION);
    }

    protected Organisation findOrganisation(final String organisationIdentification) throws FunctionalException {
        Organisation organisation;
        try {
            organisation = this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.DOMAIN_DISTRIBUTION_AUTOMATION, e);
        }
        return organisation;
    }

    protected RtuDevice findRtuDeviceForDevice(final Device device) {
        return this.rtuDeviceRepository.findById(device.getId());
    }

    protected OsgpException ensureOsgpException(final Throwable t, final String defaultMessage) {
        if (t instanceof OsgpException) {
            return (OsgpException) t;
        }

        return new TechnicalException(ComponentType.DOMAIN_DISTRIBUTION_AUTOMATION, defaultMessage, t);
    }

    protected void handleResponseMessageReceived(final Logger logger, final String deviceIdentification) {
        try {
            final RtuDevice device = this.rtuDeviceRepository.findByDeviceIdentification(deviceIdentification);
            if (this.shouldUpdateCommunicationTime(device, this.lastCommunicationUpdateInterval)) {
                device.messageReceived();
                this.rtuDeviceRepository.save(device);
            } else {
                logger.info("Last communication time within {} seconds. Skipping last communication date update.",
                        this.lastCommunicationUpdateInterval);
            }
        } catch (final OptimisticLockException ex) {
            logger.warn("Last communication time not updated due to optimistic lock exception", ex);
        }
    }

    protected boolean shouldUpdateCommunicationTime(final RtuDevice device, final int lastCommunicationUpdateInterval) {
        final DateTime timeToCheck = DateTime.now().minusSeconds(lastCommunicationUpdateInterval);
        final DateTime timeOfLastCommunication = new DateTime(device.getLastCommunicationTime());
        return timeOfLastCommunication.isBefore(timeToCheck);
    }

    protected static String getCorrelationId(final String organisationIdentification, final String deviceIdentification) {

        return organisationIdentification + "|||" + deviceIdentification + "|||" + UUID.randomUUID().toString();
    }

}
