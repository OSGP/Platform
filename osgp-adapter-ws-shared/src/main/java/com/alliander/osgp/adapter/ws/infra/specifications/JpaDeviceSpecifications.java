/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.infra.specifications;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DeviceAuthorization;
import com.alliander.osgp.domain.core.entities.DeviceFirmware;
import com.alliander.osgp.domain.core.entities.DeviceModel;
import com.alliander.osgp.domain.core.entities.Manufacturer;
import com.alliander.osgp.domain.core.entities.Organisation;
import com.alliander.osgp.domain.core.exceptions.ArgumentNullOrEmptyException;
import com.alliander.osgp.domain.core.specifications.DeviceSpecifications;
import com.alliander.osgp.domain.core.valueobjects.DeviceFunctionGroup;
import com.alliander.osgp.domain.core.valueobjects.FirmwareModuleFilterType;

public class JpaDeviceSpecifications implements DeviceSpecifications {

    @Override
    public Specification<Device> hasTechnicalInstallationDate() throws ArgumentNullOrEmptyException {
        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.isNotNull(deviceRoot.<Date> get("technicalInstallationDate"));
            }
        };
    }

    @Override
    public Specification<Device> forOrganisation(final Organisation organisation) throws ArgumentNullOrEmptyException {
        if (organisation == null) {
            throw new ArgumentNullOrEmptyException("organisation");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                final Subquery<Long> subquery = query.subquery(Long.class);
                final Root<DeviceAuthorization> deviceAuthorizationRoot = subquery.from(DeviceAuthorization.class);
                subquery.select(deviceAuthorizationRoot.get("device").get("id").as(Long.class));
                subquery.where(cb.equal(deviceAuthorizationRoot.get("organisation"), organisation.getId()));

                return cb.in(deviceRoot.get("id")).value(subquery);
            }
        };
    }

    @Override
    public Specification<Device> hasDeviceIdentification(final String deviceIdentification, final boolean exactMatch)
            throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(deviceIdentification)) {
            throw new ArgumentNullOrEmptyException("deviceIdentification");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {
                if (exactMatch) {
                    return cb.equal(cb.upper(deviceRoot.<String> get("deviceIdentification")), deviceIdentification);
                } else {
                    return cb.like(cb.upper(deviceRoot.<String> get("deviceIdentification")),
                            deviceIdentification.toUpperCase());
                }
            }
        };
    }

    @Override
    public Specification<Device> hasCity(final String city) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(city)) {
            throw new ArgumentNullOrEmptyException("city");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("containerCity")), city.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> hasPostalCode(final String postalCode) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(postalCode)) {
            throw new ArgumentNullOrEmptyException("postalCode");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("containerPostalCode")), postalCode.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> hasStreet(final String street) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(street)) {
            throw new ArgumentNullOrEmptyException("street");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("containerStreet")), street.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> hasNumber(final String number) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(number)) {
            throw new ArgumentNullOrEmptyException("number");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("containerNumber")), number.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> hasMunicipality(final String municipality) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(municipality)) {
            throw new ArgumentNullOrEmptyException("municipality");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("containerMunicipality")), municipality.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> hasAlias(final String alias) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(alias)) {
            throw new ArgumentNullOrEmptyException("alias");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("alias")), alias.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> isManagedExternally(final Boolean isManagedExternally)
            throws ArgumentNullOrEmptyException {
        if (isManagedExternally == null) {
            throw new ArgumentNullOrEmptyException("isManagedExternally");
        }

        return new Specification<Device>() {

            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {
                final Subquery<Long> subquery = query.subquery(Long.class);
                final Root<DeviceAuthorization> deviceAuthorizationRoot = subquery.from(DeviceAuthorization.class);
                subquery.select(cb.countDistinct(deviceAuthorizationRoot));
                subquery.where(cb.equal(deviceAuthorizationRoot.get("device"), deviceRoot.<Long> get("id")));
                if (isManagedExternally) {
                    return cb.greaterThan(subquery, Long.valueOf(1));
                } else {
                    return cb.lessThanOrEqualTo(subquery, Long.valueOf(1));
                }
            }
        };
    }

    @Override
    public Specification<Device> isActived(final Boolean activated) throws ArgumentNullOrEmptyException {
        if (activated == null) {
            throw new ArgumentNullOrEmptyException("activated");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.equal(deviceRoot.<Boolean> get("isActivated"), activated);
            }
        };
    }

    @Override
    public Specification<Device> isInMaintetance(final Boolean inMaintenance) throws ArgumentNullOrEmptyException {
        if (inMaintenance == null) {
            throw new ArgumentNullOrEmptyException("inMaintenance");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.equal(deviceRoot.<Boolean> get("inMaintenance"), inMaintenance);
            }
        };
    }

    @Override
    public Specification<Device> forOwner(final String organisation) throws ArgumentNullOrEmptyException {
        if (organisation == null) {
            throw new ArgumentNullOrEmptyException("owner");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                final Subquery<Long> subquery = query.subquery(Long.class);
                final Root<DeviceAuthorization> deviceAuthorizationRoot = subquery.from(DeviceAuthorization.class);
                subquery.select(deviceAuthorizationRoot.get("device").get("id").as(Long.class));
                subquery.where(cb.and(
                        cb.like(cb.upper(deviceAuthorizationRoot.get("organisation").<String> get("name")),
                                organisation.toUpperCase()),
                        cb.equal(deviceAuthorizationRoot.get("functionGroup"), DeviceFunctionGroup.OWNER.ordinal())));
                return cb.in(deviceRoot.get("id")).value(subquery);
            }
        };
    }

    @Override
    public Specification<Device> forDeviceType(final String deviceType) throws ArgumentNullOrEmptyException {
        if (deviceType == null) {
            throw new ArgumentNullOrEmptyException("deviceType");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.like(cb.upper(deviceRoot.<String> get("deviceType")), deviceType.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> forDeviceModel(final String deviceModel) throws ArgumentNullOrEmptyException {
        if (deviceModel == null) {
            throw new ArgumentNullOrEmptyException("deviceModel");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {
                return cb.like(cb.upper(deviceRoot.<String> get("deviceModel").get("modelCode").as(String.class)),
                        deviceModel.toUpperCase());
            }
        };
    }

    @Override
    public Specification<Device> forManufacturer(final Manufacturer manufacturer) throws ArgumentNullOrEmptyException {
        if (manufacturer == null) {
            throw new ArgumentNullOrEmptyException("manufacturer");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                final Subquery<Long> subquery = query.subquery(Long.class);
                final Root<DeviceModel> deviceModelRoot = subquery.from(DeviceModel.class);
                subquery.select(deviceModelRoot.get("id").as(Long.class));
                subquery.where(cb.equal(cb.upper(deviceModelRoot.get("manufacturerId").<String> get("name")),
                        manufacturer.getName().toUpperCase()));
                return cb.in(deviceRoot.get("deviceModel").get("id").as(Long.class)).value(subquery);
            }
        };
    }

    @Override
    public Specification<Device> forFirmwareModuleVersion(final FirmwareModuleFilterType firmwareModuleFilterType,
            final String firmwareModuleVersion) throws ArgumentNullOrEmptyException {
        if (StringUtils.isEmpty(firmwareModuleVersion)) {
            throw new ArgumentNullOrEmptyException("firmwareModuleVersion");
        }
        if (firmwareModuleFilterType == null) {
            throw new ArgumentNullOrEmptyException("firmwareModuleType");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {
                String moduleFieldName = "";
                switch (firmwareModuleFilterType) {
                case COMMUNICATION:
                    moduleFieldName = "moduleVersionComm";
                    break;
                case FUNCTIONAL:
                    moduleFieldName = "moduleVersionFunc";
                    break;
                case SECURITY:
                    moduleFieldName = "moduleVersionSec";
                    break;
                case M_BUS:
                    moduleFieldName = "moduleVersionMbus";
                    break;
                case MODULE_ACTIVE:
                    moduleFieldName = "moduleVersionMa";
                    break;
                case ACTIVE_FIRMWARE:
                    break;

                default:
                    break;
                }

                final Subquery<Long> subquery = query.subquery(Long.class);
                final Root<DeviceFirmware> deviceFirmwareRoot = subquery.from(DeviceFirmware.class);
                subquery.select(deviceFirmwareRoot.get("device").get("id").as(Long.class));
                subquery.where(cb.and(
                        cb.like(cb.upper(deviceFirmwareRoot.get("firmware").<String> get(moduleFieldName)),
                                firmwareModuleVersion.toUpperCase()),
                        cb.equal(deviceFirmwareRoot.<Boolean> get("active"), true)));
                return cb.in(deviceRoot.get("id").as(Long.class)).value(subquery);
            }
        };
    }

    @Override
    public final Specification<Device> existsInDeviceIdentificationList(final List<String> deviceIdentifications)
            throws ArgumentNullOrEmptyException {
        if (deviceIdentifications == null) {
            throw new ArgumentNullOrEmptyException("deviceIdentifications");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.in(deviceRoot.get("deviceIdentification")).value(deviceIdentifications);
            }
        };
    }

    @Override
    public final Specification<Device> excludeDeviceIdentificationList(final List<String> deviceIdentifications)
            throws ArgumentNullOrEmptyException {
        if (deviceIdentifications == null) {
            throw new ArgumentNullOrEmptyException("deviceIdentifications");
        }

        return new Specification<Device>() {
            @Override
            public Predicate toPredicate(final Root<Device> deviceRoot, final CriteriaQuery<?> query,
                    final CriteriaBuilder cb) {

                return cb.not(deviceRoot.get("deviceIdentification").in(deviceIdentifications));
            }
        };
    }
}
