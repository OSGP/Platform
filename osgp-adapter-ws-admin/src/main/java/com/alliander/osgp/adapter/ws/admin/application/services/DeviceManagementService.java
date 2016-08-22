/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.admin.application.services;

import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.ws.admin.infra.jms.AdminRequestMessage;
import com.alliander.osgp.adapter.ws.admin.infra.jms.AdminRequestMessageSender;
import com.alliander.osgp.adapter.ws.admin.infra.jms.AdminRequestMessageType;
import com.alliander.osgp.adapter.ws.admin.infra.jms.AdminResponseMessageFinder;
import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceAuthorization;
import com.alliander.osgp.domain.core.entities.Event;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.entities.ProtocolInfo;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.exceptions.ArgumentNullOrEmptyException;
import com.alliander.osgp.domain.core.exceptions.EmptyOwnerException;
import com.alliander.osgp.domain.core.exceptions.ExistingEntityException;
import com.alliander.osgp.domain.core.exceptions.NotAuthorizedException;
import com.alliander.osgp.domain.core.exceptions.UnknownEntityException;
import com.alliander.osgp.domain.core.exceptions.ValidationException;
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.EventRepository;
import com.alliander.osgp.domain.core.repositories.OrganisationRepository;
import com.alliander.osgp.domain.core.repositories.ProtocolInfoRepository;
import com.alliander.osgp.domain.core.services.CorrelationIdProviderService;
import com.alliander.osgp.domain.core.services.DeviceDomainService;
import com.alliander.osgp.domain.core.services.OrganisationDomainService;
import com.alliander.osgp.domain.core.services.SecurityService;
import com.alliander.osgp.domain.core.specifications.DeviceSpecifications;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.domain.core.validation.PublicKey;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunction;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;
import com.alliander.osgp.domain.core.valueobjects.PlatformDomain;
import com.alliander.osgp.domain.core.valueobjects.PlatformFunction;
import com.alliander.osgp.domain.core.valueobjects.PlatformFunctionGroup;
import com.alliander.osgp.logging.domain.entities.DeviceLogItem;
import com.alliander.osgp.logging.domain.repositories.DeviceLogItemRepository;
import com.alliander.osgp.shared.application.config.PagingSettings;
import com.alliander.osgp.shared.exceptionhandling.ComponentType;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.FunctionalExceptionType;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;

@Service(value = "wsAdminDeviceManagementService")
@Transactional(value = "transactionManager")
@Validated
public class DeviceManagementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManagementService.class);

    @Autowired
    private PagingSettings pagingSettings;

    @Autowired
    private OrganisationDomainService organisationDomainService;

    @Autowired
    private DeviceDomainService deviceDomainService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceSpecifications deviceSpecifications;

    @Autowired
    private DeviceLogItemRepository logItemRepository;

    @Autowired
    private DeviceAuthorizationRepository authorizationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CorrelationIdProviderService correlationIdProviderService;

    @Autowired
    private AdminRequestMessageSender adminRequestMessageSender;

    @Autowired
    private AdminResponseMessageFinder adminResponseMessageFinder;

    @Autowired
    private ProtocolInfoRepository protocolRepository;

    /**
     * Constructor
     */
    public DeviceManagementService() {
        // Parameterless constructor required for transactions...
    }

    public void addOrganisation(@Identification final String organisationIdentification,
            @Valid final Organisation newOrganisation) throws FunctionalException {

        LOGGER.debug("addOrganisation called with organisation {} and new organisation {}", organisationIdentification,
                newOrganisation != null ? newOrganisation.getOrganisationIdentification() : "null");

        final Organisation organisation = this.findOrganisation(organisationIdentification);

        this.isAllowed(organisation, PlatformFunction.CREATE_ORGANISATION);

        try {
            // Save the organisation.
            this.organisationRepository.save(newOrganisation);
        } catch (final JpaSystemException ex) {
            if (ex.getCause() instanceof PersistenceException) {
                LOGGER.error("Add organisation failure JpaSystemException", ex);
                throw new FunctionalException(FunctionalExceptionType.EXISTING_ORGANISATION, ComponentType.WS_ADMIN,
                        new ExistingEntityException(Organisation.class,
                                newOrganisation.getOrganisationIdentification(), ex));
            }
        }
    }

    public void removeOrganisation(@Identification final String organisationIdentification,
            @Identification final String organisationToRemoveIdentification) throws FunctionalException {

        LOGGER.debug("removeOrganisation called with organisation {} and organisation to remove {}",
                organisationIdentification, organisationToRemoveIdentification);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        final Organisation organisationToRemove = this.findOrganisation(organisationToRemoveIdentification);

        this.isAllowed(organisation, PlatformFunction.REMOVE_ORGANISATION);

        try {
            final List<DeviceAuthorization> deviceAuthorizations = this.authorizationRepository
                    .findByOrganisation(organisationToRemove);
            if (!deviceAuthorizations.isEmpty()) {
                throw new FunctionalException(FunctionalExceptionType.EXISTING_DEVICE_AUTHORIZATIONS,
                        ComponentType.WS_ADMIN, new ValidationException(String.format(
                                "Device Authorizations are still present for the current organisation %s",
                                organisationToRemove.getOrganisationIdentification())));
            }

            organisationToRemove.setIsEnabled(false);
            this.organisationRepository.save(organisationToRemove);
        } catch (final JpaSystemException ex) {
            if (ex.getCause() instanceof PersistenceException) {
                LOGGER.error("Remove organisation failure JpaSystemException", ex);
                throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.WS_ADMIN,
                        new UnknownEntityException(Organisation.class, organisationToRemoveIdentification, ex));
            }
        }
    }

    public void activateOrganisation(@Identification final String organisationIdentification,
            @Identification final String organisationIdentificationToActivate) throws FunctionalException {

        LOGGER.debug("activateOrganisation called with organisation {} and organisation to activate {}",
                organisationIdentification, organisationIdentificationToActivate);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        final Organisation organisationToActivate = this.findOrganisation(organisationIdentificationToActivate);

        this.isAllowed(organisation, PlatformFunction.CHANGE_ORGANISATION);

        try {
            organisationToActivate.setIsEnabled(true);
            this.organisationRepository.save(organisationToActivate);
        } catch (final JpaSystemException ex) {
            if (ex.getCause() instanceof PersistenceException) {
                LOGGER.error("activate organisation failure JpaSystemException", ex);
                throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.WS_ADMIN,
                        new UnknownEntityException(Organisation.class, organisationIdentificationToActivate, ex));
            }
        }
    }

    public void changeOrganisation(@Identification final String organisationIdentification,
            @Identification final String organisationToBeChangedIdentification,
            @Identification final String newOrganisationIdentification, final String newOrganisationName,
            @NotNull final PlatformFunctionGroup newOrganisationPlatformFunctionGroup,
            @NotNull final List<PlatformDomain> newDomains) throws FunctionalException {

        LOGGER.info(
                "changeOrganisation called with organisation {} and organisation to change {}. new values for organisationIdentification {}, organisationName {}, organisationPlatformFunctionGroup {}",
                organisationIdentification, organisationToBeChangedIdentification, newOrganisationIdentification,
                newOrganisationName, newOrganisationPlatformFunctionGroup);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.CHANGE_ORGANISATION);

        try {
            this.checkIfNewOrganisationIdentificationAlreadyExists(organisationToBeChangedIdentification,
                    newOrganisationIdentification);

            final Organisation organisationToChange = this.findOrganisation(organisationToBeChangedIdentification);
            organisationToChange.changeOrganisationData(newOrganisationIdentification, newOrganisationName,
                    newOrganisationPlatformFunctionGroup);
            organisationToChange.setDomains(newDomains);

            this.organisationRepository.save(organisationToChange);
        } catch (final JpaSystemException ex) {
            if (ex.getCause() instanceof PersistenceException) {
                LOGGER.error("change organisation failure JpaSystemException", ex);
                throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.WS_ADMIN,
                        new UnknownEntityException(Organisation.class, organisationToBeChangedIdentification, ex));
            }
        }
    }

    private void checkIfNewOrganisationIdentificationAlreadyExists(final String organisationToBeChangedIdentification,
            final String newOrganisationIdentification) throws FunctionalException {
        if (!organisationToBeChangedIdentification.equals(newOrganisationIdentification)) {
            Organisation org = null;
            try {
                org = this.findOrganisation(newOrganisationIdentification);
            } catch (final Exception e) {
                LOGGER.debug("newOrganisationIdentification not yet registered, proceed with changeOrganisation", e);
            }

            if (org != null) {
                throw new FunctionalException(
                        FunctionalExceptionType.EXISTING_ORGANISATION_WITH_SAME_IDENTIFICATION,
                        ComponentType.WS_ADMIN,
                        new ValidationException(
                                String.format(
                                        "The new organisation identification cannot be applied: an organisation is already registered with organisation identification %s",
                                        newOrganisationIdentification)));
            }
        }
    }

    public void addDeviceAuthorization(@Identification final String ownerOrganisationIdentification,
            @Identification final String organisationIdentification, @Identification final String deviceIdentification,
            @NotNull final DeviceFunctionGroup group) throws FunctionalException {

        if (group == DeviceFunctionGroup.OWNER) {
            throw new FunctionalException(FunctionalExceptionType.METHOD_NOT_ALLOWED_FOR_OWNER, ComponentType.WS_ADMIN,
                    new OperationNotSupportedException("Owner not allowed to set via this method."));
        }

        // Check input data and authorization
        final Organisation organisation = this.findOrganisation(organisationIdentification);

        final Organisation ownerOrganisation = this.findOrganisation(ownerOrganisationIdentification);

        final Device device = this.findDevice(deviceIdentification);

        this.isAllowed(ownerOrganisation, device, DeviceFunction.SET_DEVICE_AUTHORIZATION);

        // Check if group is already set on device
        for (final DeviceAuthorization authorization : device.getAuthorizations()) {
            if (authorization.getOrganisation() == organisation && authorization.getFunctionGroup() == group) {
                LOGGER.info("Organisation {} already has authorization for group {} on device {}", new Object[] {
                        organisationIdentification, deviceIdentification, group });
                // Ignore the request, the authorization is already available
                return;
            }
        }

        // All checks pass, add new authorization
        final DeviceAuthorization authorization = device.addAuthorization(organisation, group);
        this.deviceRepository.save(device);
        this.authorizationRepository.save(authorization);

        LOGGER.info("Organisation {} now has authorization for function group {} on device {}",
                organisationIdentification, deviceIdentification, group);

    }

    public void removeDeviceAuthorization(@Identification final String ownerOrganisationIdentification,
            @Identification final String organisationIdentification, @Identification final String deviceIdentification,
            @NotNull final DeviceFunctionGroup group) throws FunctionalException {

        if (group == DeviceFunctionGroup.OWNER) {
            throw new FunctionalException(FunctionalExceptionType.METHOD_NOT_ALLOWED_FOR_OWNER, ComponentType.WS_ADMIN,
                    new OperationNotSupportedException("Owner not allowed to set via this method."));
        }

        // Check input data and authorization
        final Organisation organisation = this.findOrganisation(organisationIdentification);

        final Organisation ownerOrganisation = this.findOrganisation(ownerOrganisationIdentification);

        final Device device = this.findDevice(deviceIdentification);

        this.isAllowed(ownerOrganisation, device, DeviceFunction.SET_DEVICE_AUTHORIZATION);

        // All checks pass, remove authorization
        device.removeAuthorization(organisation, group);
        this.deviceRepository.save(device);
        this.authorizationRepository.deleteByDeviceAndFunctionGroup(device, group);

        LOGGER.info("Organisation {} now no longer has authorization for function group {} on device {}",
                organisationIdentification, deviceIdentification, group);
    }

    /**
     * Get all devices which have no owner.
     *
     * @return All devices which have no owner.
     * @throws NotAuthorizedException
     * @throws UnknownOrganisationException
     * @throws EmptyOwnerException
     * @throws UnknownEntityException
     * @throws ArgumentNullOrEmptyException
     * @throws ValidationException
     */
    public List<Device> findDevicesWhichHaveNoOwner(@Identification final String organisationIdentification)
            throws FunctionalException {
        final Organisation organisation = this.findOrganisation(organisationIdentification);

        this.isAllowed(organisation, PlatformFunction.GET_DEVICE_NO_OWNER);

        return this.deviceRepository.findDevicesWithNoOwner();
    }

    public List<DeviceAuthorization> findDeviceAuthorisations(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification) throws FunctionalException {

        LOGGER.debug("findDeviceAuthorisations called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final Organisation organisation = this.findOrganisation(organisationIdentification);

        final Device device = this.findDevice(deviceIdentification);

        this.isAllowed(organisation, device, DeviceFunction.GET_DEVICE_AUTHORIZATION);

        return this.authorizationRepository.findByDeviceForOrganisation(device, organisation);
    }

    public Page<DeviceLogItem> findOslpMessages(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, @Min(value = 0) final int pageNumber)
                    throws FunctionalException {

        LOGGER.debug("findOslpMessage called with organisation {}, device {} and pagenumber {}", new Object[] {
                organisationIdentification, deviceIdentification, pageNumber });

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.GET_MESSAGES);

        final PageRequest request = new PageRequest(pageNumber, this.pagingSettings.getMaximumPageSize(),
                Sort.Direction.DESC, "modificationTime");

        if (deviceIdentification != null && !deviceIdentification.isEmpty()) {
            return this.logItemRepository.findByDeviceIdentification(deviceIdentification, request);
        }

        return this.logItemRepository.findAll(request);
    }

    // === REMOVE DEVICE ===

    /**
     * @throws FunctionalException
     * @throws NotAuthorizedException
     * @throws FunctionalException
     *             Remove a device
     *
     * @param organisationIdentification
     * @param deviceIdentification
     * @throws UnknownEntityException
     * @throws ArgumentNullOrEmptyException
     * @throws UnregisteredDeviceException
     * @throws
     */
    public void removeDevice(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification) throws FunctionalException {
        final Organisation organisation = this.findOrganisation(organisationIdentification);
        final Device device = this.findDevice(deviceIdentification);
        this.isAllowed(organisation, device, DeviceFunction.REMOVE_DEVICE);

        // First remove all authorizations
        final List<DeviceAuthorization> authorisations = this.authorizationRepository.findByDevice(device);
        for (final DeviceAuthorization authorisation : authorisations) {
            this.authorizationRepository.delete(authorisation);
        }

        // Remove all events
        final List<Event> events = this.eventRepository.findByDevice(device);
        for (final Event event : events) {
            this.eventRepository.delete(event);
        }

        // Then remove the device.
        this.deviceRepository.delete(device);
    }

    // === SET OWNER ===

    /**
     * Sets the owner of the device
     *
     * @param organisationIdentification
     *            The organisation identification who performs the action
     *            (needed for security)
     * @param deviceIdentification
     *            The device identification of the device
     * @param newOwner
     *            The organisation identification of the new owner.
     * @throws UnknownEntityException
     * @throws NotAuthorizedException
     * @throws FunctionalException
     */
    public void setOwner(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, @Identification final String newOwner)
                    throws FunctionalException {
        Organisation organisation = this.findOrganisation(organisationIdentification);
        final Device device = this.findDevice(deviceIdentification);
        this.isAllowed(organisation, PlatformFunction.SET_OWNER);

        organisation = this.findOrganisation(newOwner);

        // First remove any other owners.
        final List<DeviceAuthorization> owners = this.authorizationRepository.findByDeviceAndFunctionGroup(device,
                DeviceFunctionGroup.OWNER);
        if (!owners.isEmpty()) {
            for (final DeviceAuthorization owner : owners) {
                this.authorizationRepository.delete(owner);
            }
        }

        // Now add the authorization
        final DeviceAuthorization authorization = new DeviceAuthorization(device, organisation,
                DeviceFunctionGroup.OWNER);
        this.authorizationRepository.save(authorization);
    }

    // === UPDATE KEY ===

    public void updateKey(final String organisationIdentification, @Identification final String deviceIdentification,
            @PublicKey final String publicKey, final Long protocolInfoId) throws FunctionalException {

        LOGGER.debug("Updating key for device [{}] on behalf of organisation [{}]", deviceIdentification,
                organisationIdentification);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.UPDATE_KEY);

        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);

        // TODO: refactor device creation with owner authorization and default
        // protocol and move to domain adapter project!
        if (device == null) {
            // Device not found, create new device
            LOGGER.debug("Device [{}] does not exist, creating new device", deviceIdentification);
            final Ssld ssld = new Ssld(deviceIdentification);

            final DeviceAuthorization authorization = ssld.addAuthorization(organisation, DeviceFunctionGroup.OWNER);

            final ProtocolInfo protocolInfo = this.protocolRepository.findOne(protocolInfoId);
            ssld.updateProtocol(protocolInfo);

            this.authorizationRepository.save(authorization);
        }

        this.enqueueUpdateKeyRequest(organisationIdentification, deviceIdentification, publicKey);
    }

    public String enqueueUpdateKeyRequest(final String organisationIdentification,
            @Identification final String deviceIdentification, @PublicKey final String publicKey) {

        LOGGER.debug("enqueueUpdateKeyRequest called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        final AdminRequestMessage message = new AdminRequestMessage(AdminRequestMessageType.UPDATE_KEY, correlationUid,
                organisationIdentification, deviceIdentification, publicKey);

        this.adminRequestMessageSender.send(message);

        return correlationUid;
    }

    public ResponseMessage dequeueUpdateKeyResponse(final String correlationUid) throws OsgpException {

        return this.adminResponseMessageFinder.findMessage(correlationUid);
    }

    // === REVOKE KEY ===

    public void revokeKey(final String organisationIdentification, @Identification final String deviceIdentification)
            throws FunctionalException {

        LOGGER.debug("Revoking key for device [{}] on behalf of organisation [{}]", deviceIdentification,
                organisationIdentification);

        this.findDevice(deviceIdentification);
        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.REVOKE_KEY);

        this.enqueueRevokeKeyRequest(organisationIdentification, deviceIdentification);
    }

    public String enqueueRevokeKeyRequest(final String organisationIdentification,
            @Identification final String deviceIdentification) {

        LOGGER.debug("enqueueRevokeKeyRequest called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        final AdminRequestMessage message = new AdminRequestMessage(AdminRequestMessageType.REVOKE_KEY, correlationUid,
                organisationIdentification, deviceIdentification, null);

        this.adminRequestMessageSender.send(message);

        return correlationUid;
    }

    public ResponseMessage dequeueRevokeKeyResponse(final String correlationUid) throws OsgpException {

        return this.adminResponseMessageFinder.findMessage(correlationUid);
    }

    public List<ProtocolInfo> getProtocolInfos(final String organisationIdentification) throws FunctionalException {

        LOGGER.debug("Retrieving all protocol infos on behalf of organisation: {}", organisationIdentification);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.GET_PROTOCOL_INFOS);

        return this.protocolRepository.findAll(new Sort(Direction.ASC, "protocol", "protocolVersion"));
    }

    public void updateDeviceProtocol(final String organisationIdentification,
            @Identification final String deviceIdentification, final String protocol, final String protocolVersion)
                    throws FunctionalException {

        LOGGER.debug("Updating protocol for device [{}] on behalf of organisation [{}] to protocol: {}, version: {}",
                deviceIdentification, organisationIdentification, protocol, protocolVersion);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.UPDATE_DEVICE_PROTOCOL);

        final Device device = this.findDevice(deviceIdentification);
        final ProtocolInfo protocolInfo = this.findProtocolInfo(protocol, protocolVersion);

        if (protocolInfo.equals(device.getProtocolInfo())) {
            LOGGER.info("Not updating protocol: {}, version: {} on device {} since it is already configured", protocol,
                    protocolVersion, deviceIdentification);
            return;
        }

        device.updateProtocol(protocolInfo);
        this.deviceRepository.save(device);

        LOGGER.info("Organisation {} configured protocol: {}, version: {} on device {}", organisationIdentification,
                protocol, protocolVersion, deviceIdentification);
    }

    public String enqueueDeactivateDeviceRequest(final String organisationIdentification,
            final String deviceIdentification) {

        // TODO: bypassing authorization logic for now, needs to be fixed.

        LOGGER.debug("enqueueDeactivateDevice called with organisation {} and device {}", organisationIdentification,
                deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        final AdminRequestMessage message = new AdminRequestMessage(AdminRequestMessageType.DEACTIVATE_DEVICE,
                correlationUid, organisationIdentification, deviceIdentification, null);

        this.adminRequestMessageSender.send(message);

        return correlationUid;
    }

    public void deactivateDeviceRequest(final String organisationIdentification,
            @Identification final String deviceIdentification) throws FunctionalException {

        LOGGER.debug("Deactivating device [{}] on behalf of organisation [{}]", deviceIdentification,
                organisationIdentification);

        final Organisation organisation = this.findOrganisation(organisationIdentification);
        this.isAllowed(organisation, PlatformFunction.DEACTIVATE_DEVICE);

        if (this.deviceRepository.findByDeviceIdentification(deviceIdentification) == null) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_DEVICE, ComponentType.WS_ADMIN);
        }

        this.enqueueDeactivateDeviceRequest(organisationIdentification, deviceIdentification);
    }

    private Device findDevice(final String deviceIdentification) throws FunctionalException {
        Device device;
        try {
            device = this.deviceDomainService.searchDevice(deviceIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_DEVICE, ComponentType.WS_ADMIN, e);
        }
        return device;
    }

    private ProtocolInfo findProtocolInfo(final String protocol, final String protocolVersion)
            throws FunctionalException {
        final ProtocolInfo protocolInfo = this.protocolRepository.findByProtocolAndProtocolVersion(protocol,
                protocolVersion);
        if (protocolInfo == null) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_PROTOCOL_NAME_OR_VERSION,
                    ComponentType.WS_ADMIN);
        }
        return protocolInfo;
    }

    private Organisation findOrganisation(final String organisationIdentification) throws FunctionalException {
        Organisation organisation;
        try {
            organisation = this.organisationDomainService.searchOrganisation(organisationIdentification);
        } catch (final UnknownEntityException e) {
            throw new FunctionalException(FunctionalExceptionType.UNKNOWN_ORGANISATION, ComponentType.WS_ADMIN, e);
        }
        return organisation;
    }

    private void isAllowed(final Organisation organisation, final PlatformFunction platformFunction)
            throws FunctionalException {
        try {
            this.securityService.checkAuthorization(organisation, platformFunction);
        } catch (final NotAuthorizedException e) {
            throw new FunctionalException(FunctionalExceptionType.UNAUTHORIZED, ComponentType.WS_ADMIN, e);
        }
    }

    private void isAllowed(final Organisation organisation, final Device device, final DeviceFunction deviceFunction)
            throws FunctionalException {
        try {
            this.securityService.checkAuthorization(organisation, device, deviceFunction);
        } catch (final NotAuthorizedException e) {
            throw new FunctionalException(FunctionalExceptionType.UNAUTHORIZED, ComponentType.WS_ADMIN, e);
        }

    }

}
