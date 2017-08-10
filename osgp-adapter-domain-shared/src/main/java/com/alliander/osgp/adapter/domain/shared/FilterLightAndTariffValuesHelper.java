/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alliander.osgp.domain.core.entities.DeviceOutputSetting;
import com.alliander.osgp.domain.core.valueobjects.DomainType;
import com.alliander.osgp.domain.core.valueobjects.LightValue;
import com.alliander.osgp.domain.core.valueobjects.RelayType;
import com.alliander.osgp.domain.core.valueobjects.TariffValue;

public class FilterLightAndTariffValuesHelper {

    /**
     * Filter light values based on PublicLighting domain. Only matching values
     * will be returned.
     *
     * @param source
     *            list to filter
     * @param dosMap
     *            mapping of output settings
     * @param allowedDomainType
     *            type of domain allowed
     * @return list with filtered values or empty list when domain is not
     *         allowed.
     */
    public static List<LightValue> filterLightValues(final List<LightValue> source,
            final Map<Integer, DeviceOutputSetting> dosMap, final DomainType allowedDomainType) {

        final List<LightValue> filteredValues = new ArrayList<>();
        if (allowedDomainType != DomainType.PUBLIC_LIGHTING) {
            // Return empty list
            return filteredValues;
        }

        for (final LightValue lv : source) {
            if (dosMap.containsKey(lv.getIndex())
                    && dosMap.get(lv.getIndex()).getOutputType().domainType().equals(allowedDomainType)) {
                filteredValues.add(lv);
            }
        }

        return filteredValues;
    }

    /**
     * Filter light values based on TariffSwitching domain. Only matching values
     * will be returned.
     *
     * @param source
     *            list to filter
     * @param dosMap
     *            mapping of output settings
     * @param allowedDomainType
     *            type of domain allowed
     * @return list with filtered values or empty list when domain is not
     *         allowed.
     */
    public static List<TariffValue> filterTariffValues(final List<LightValue> source,
            final Map<Integer, DeviceOutputSetting> dosMap, final DomainType allowedDomainType) {

        final List<TariffValue> filteredValues = new ArrayList<>();
        if (allowedDomainType != DomainType.TARIFF_SWITCHING) {
            // Return empty list
            return filteredValues;
        }

        for (final LightValue lv : source) {
            if (dosMap.containsKey(lv.getIndex())
                    && dosMap.get(lv.getIndex()).getOutputType().domainType().equals(allowedDomainType)) {
                // Map light value to tariff value
                final TariffValue tf = new TariffValue();
                tf.setIndex(lv.getIndex());
                if (dosMap.get(lv.getIndex()).getOutputType().equals(RelayType.TARIFF_REVERSED)) {
                    // Reversed means copy the 'isOn' value to the 'isHigh'
                    // value without inverting the boolean value
                    tf.setHigh(lv.isOn());
                } else {
                    // Not reversed means copy the 'isOn' value to the 'isHigh'
                    // value inverting the boolean value
                    tf.setHigh(!lv.isOn());
                }

                filteredValues.add(tf);
            }
        }

        return filteredValues;
    }

}
