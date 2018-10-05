/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.joda.time.DateTime;

import org.opensmartgridplatform.domain.core.validation.LightTypeAndConfiguration;
import org.opensmartgridplatform.domain.core.validation.LongTermIntervalAndLongTermIntervalType;
import org.opensmartgridplatform.domain.core.validation.ShortTermHistoryIntervalMinutes;
import org.opensmartgridplatform.domain.core.validation.TlsConfiguration;

@LightTypeAndConfiguration
@LongTermIntervalAndLongTermIntervalType
@ShortTermHistoryIntervalMinutes
@TlsConfiguration
public class Configuration implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 8359276160483972289L;

    private final LightType lightType;

    @Valid
    private final DaliConfiguration daliConfiguration;

    @Valid
    private final RelayConfiguration relayConfiguration;

    @Min(0)
    private final Integer shortTermHistoryIntervalMinutes;

    @Min(0)
    private final Integer longTermHistoryInterval;

    private final LongTermIntervalType longTermHistoryIntervalType;

    private final LinkType preferredLinkType;

    private final MeterType meterType;

    private Integer timeSyncFrequency;

    private DeviceFixedIp deviceFixedIp;

    private Boolean dhcpEnabled;

    private Boolean tlsEnabled;

    private Integer tlsPortNumber;

    private String commonNameString;

    private Integer communicationTimeout;

    private Integer communicationNumberOfRetries;

    private Integer communicationPauseTimeBetweenConnectionTrials;

    private String osgpIpAddress;

    private Integer osgpPortNumber;

    private String ntpHost;

    private Boolean ntpEnabled;

    private Integer ntpSyncInterval;

    private Boolean testButtonEnabled;

    private Boolean automaticSummerTimingEnabled;

    private Integer astroGateSunRiseOffset;

    private Integer astroGateSunSetOffset;

    private List<Integer> switchingDelays;

    private List<RelayMatrix> relayLinking;

    private Boolean relayRefreshing;

    private DateTime summerTimeDetails;

    private DateTime winterTimeDetails;

    public Configuration(Configuration.Builder builder) {
        this.lightType = builder.lightType;
        this.daliConfiguration = builder.daliConfiguration;
        this.relayConfiguration = builder.relayConfiguration;
        this.shortTermHistoryIntervalMinutes = builder.shortTermHistoryIntervalMinutes;
        this.preferredLinkType = builder.preferredLinkType;
        this.meterType = builder.meterType;
        this.longTermHistoryInterval = builder.longTermHistoryInterval;
        this.longTermHistoryIntervalType = builder.longTermHistoryIntervalType;
        this.timeSyncFrequency = builder.timeSyncFrequency;
        this.deviceFixedIp = builder.deviceFixedIp;
        this.dhcpEnabled = builder.dhcpEnabled;
        this.tlsEnabled = builder.tlsEnabled;
        this.tlsPortNumber = builder.tlsPortNumber;
        this.commonNameString = builder.commonNameString;
        this.communicationTimeout = builder.communicationTimeout;
        this.communicationNumberOfRetries = builder.communicationNumberOfRetries;
        this.communicationPauseTimeBetweenConnectionTrials = builder.communicationPauseTimeBetweenConnectionTrials;
        this.osgpIpAddress = builder.osgpIpAddress;
        this.osgpPortNumber = builder.osgpPortNumber;
        this.ntpHost = builder.ntpHost;
        this.ntpEnabled = builder.ntpEnabled;
        this.ntpSyncInterval = builder.ntpSyncInterval;
        this.testButtonEnabled = builder.testButtonEnabled;
        this.automaticSummerTimingEnabled = builder.automaticSummerTimingEnabled;
        this.astroGateSunRiseOffset = builder.astroGateSunRiseOffset;
        this.astroGateSunSetOffset = builder.astroGateSunSetOffset;
        this.switchingDelays = builder.switchingDelays;
        this.relayLinking = builder.relayLinking;
        this.relayRefreshing = builder.relayRefreshing;
        this.summerTimeDetails = builder.summerTimeDetails;
        this.winterTimeDetails = builder.winterTimeDetails;
    }

    public MeterType getMeterType() {
        return this.meterType;
    }

    public LightType getLightType() {
        return this.lightType;
    }

    public DaliConfiguration getDaliConfiguration() {
        return this.daliConfiguration;
    }

    public RelayConfiguration getRelayConfiguration() {
        return this.relayConfiguration;
    }

    public Integer getShortTermHistoryIntervalMinutes() {
        return this.shortTermHistoryIntervalMinutes;
    }

    public LinkType getPreferredLinkType() {
        return this.preferredLinkType;
    }

    public Integer getLongTermHistoryInterval() {
        return this.longTermHistoryInterval;
    }

    public LongTermIntervalType getLongTermHistoryIntervalType() {
        return this.longTermHistoryIntervalType;
    }

    public Integer getTimeSyncFrequency() {
        return this.timeSyncFrequency;
    }

    public DeviceFixedIp getDeviceFixedIp() {
        return this.deviceFixedIp;
    }

    public Boolean isDhcpEnabled() {
        return this.dhcpEnabled;
    }

    public Boolean isTlsEnabled() {
        return this.tlsEnabled;
    }

    public Integer getTlsPortNumber() {
        return this.tlsPortNumber;
    }

    public String getCommonNameString() {
        return this.commonNameString;
    }

    public Integer getCommunicationTimeout() {
        return this.communicationTimeout;
    }

    public Integer getCommunicationNumberOfRetries() {
        return this.communicationNumberOfRetries;
    }

    public Integer getCommunicationPauseTimeBetweenConnectionTrials() {
        return this.communicationPauseTimeBetweenConnectionTrials;
    }

    public String getOsgpIpAddress() {
        return this.osgpIpAddress;
    }

    public Integer getOsgpPortNumber() {
        return this.osgpPortNumber;
    }

    public String getNtpHost() {
        return this.ntpHost;
    }

    public Boolean getNtpEnabled() {
        return this.ntpEnabled;
    }

    public Integer getNtpSyncInterval() {
        return this.ntpSyncInterval;
    }

    public Boolean isTestButtonEnabled() {
        return this.testButtonEnabled;
    }

    public Boolean isAutomaticSummerTimingEnabled() {
        return this.automaticSummerTimingEnabled;
    }

    public Integer getAstroGateSunRiseOffset() {
        return this.astroGateSunRiseOffset;
    }

    public Integer getAstroGateSunSetOffset() {
        return this.astroGateSunSetOffset;
    }

    public Boolean isRelayRefreshing() {
        return this.relayRefreshing;
    }

    public List<Integer> getSwitchingDelays() {
        return this.switchingDelays;
    }

    public List<RelayMatrix> getRelayLinking() {
        return this.relayLinking;
    }

    public DateTime getSummerTimeDetails() {
        return this.summerTimeDetails;
    }

    public DateTime getWinterTimeDetails() {
        return this.winterTimeDetails;
    }

    public static class Builder {
        private LightType lightType;
        private DaliConfiguration daliConfiguration;
        private RelayConfiguration relayConfiguration;
        private Integer shortTermHistoryIntervalMinutes;
        private LinkType preferredLinkType;
        private MeterType meterType;
        private Integer longTermHistoryInterval;
        private LongTermIntervalType longTermHistoryIntervalType;
        private Integer timeSyncFrequency;
        private DeviceFixedIp deviceFixedIp;
        private Boolean dhcpEnabled;
        private Boolean tlsEnabled;
        private Integer tlsPortNumber;
        private String commonNameString;
        private Integer communicationTimeout;
        private Integer communicationNumberOfRetries;
        private Integer communicationPauseTimeBetweenConnectionTrials;
        private String osgpIpAddress;
        private Integer osgpPortNumber;
        private String ntpHost;
        private Boolean ntpEnabled;
        private Integer ntpSyncInterval;
        private Boolean testButtonEnabled;
        private Boolean automaticSummerTimingEnabled;
        private Integer astroGateSunRiseOffset;
        private Integer astroGateSunSetOffset;
        private List<Integer> switchingDelays;
        private List<RelayMatrix> relayLinking;
        private Boolean relayRefreshing;
        private DateTime summerTimeDetails;
        private DateTime winterTimeDetails;

        public Builder withLightType(LightType lightType) {
            this.lightType = lightType;
            return this;
        }

        public Builder withDaliConfiguration(DaliConfiguration daliConfiguration) {
            this.daliConfiguration = daliConfiguration;
            return this;
        }

        public Builder withRelayConfiguration(RelayConfiguration relayConfiguration) {
            this.relayConfiguration = relayConfiguration;
            return this;
        }

        public Builder withShortTemHistoryIntervalMinutes(Integer shortTermHistoryIntervalMinutes) {
            this.shortTermHistoryIntervalMinutes = shortTermHistoryIntervalMinutes;
            return this;
        }

        public Builder withPreferredLinkType(LinkType preferredLinkType) {
            this.preferredLinkType = preferredLinkType;
            return this;
        }

        public Builder withMeterType(MeterType meterType) {
            this.meterType = meterType;
            return this;
        }

        public Builder withLongTermHistoryInterval(Integer longTermHistoryInterval) {
            this.longTermHistoryInterval = longTermHistoryInterval;
            return this;
        }

        public Builder withLongTermHistoryIntervalType(LongTermIntervalType longTermHistoryIntervalType) {
            this.longTermHistoryIntervalType = longTermHistoryIntervalType;
            return this;
        }

        public Builder withTimeSyncFrequency(Integer timeSyncFrequency) {
            this.timeSyncFrequency = timeSyncFrequency;
            return this;
        }

        public Builder withDeviceFixedIp(DeviceFixedIp deviceFixedIp) {
            this.deviceFixedIp = deviceFixedIp;
            return this;
        }

        public Builder withDhcpEnabled(Boolean dhcpEnabled) {
            this.dhcpEnabled = dhcpEnabled;
            return this;
        }

        public Builder withTlsEnabled(Boolean tlsEnabled) {
            this.tlsEnabled = tlsEnabled;
            return this;
        }

        public Builder withTlsPortNumber(Integer tlsPortNumber) {
            this.tlsPortNumber = tlsPortNumber;
            return this;
        }

        public Builder withCommonNameString(String commonNameString) {
            this.commonNameString = commonNameString;
            return this;
        }

        public Builder withCommunicationTimeout(Integer communicationTimeout) {
            this.communicationTimeout = communicationTimeout;
            return this;
        }

        public Builder withCommunicationNumberOfRetries(Integer communicationNumberOfRetries) {
            this.communicationNumberOfRetries = communicationNumberOfRetries;
            return this;
        }

        public Builder withCommunicationPauseTimeBetweenConnectionTrials(Integer communicationPauseTimeBetweenConnectionTrials) {
            this.communicationPauseTimeBetweenConnectionTrials = communicationPauseTimeBetweenConnectionTrials;
            return this;
        }

        public Builder withOsgpIpAddress(String osgpIpAddress){
            this.osgpIpAddress = osgpIpAddress;
            return this;
        }

        public Builder withOsgpPortNumber(Integer osgpPortNumber) {
            this.osgpPortNumber = osgpPortNumber;
            return this;
        }

        public Builder withNtpHost(String ntpHost) {
            this.ntpHost = ntpHost;
            return this;
        }

        public Builder withNtpEnabled(Boolean ntpEnabled) {
            this.ntpEnabled = ntpEnabled;
            return this;
        }

        public Builder withNtpSyncInterval(Integer ntpSyncInterval) {
            this.ntpSyncInterval = ntpSyncInterval;
            return this;
        }

        public Builder withTestButtonEnabled(Boolean testButtonEnabled) {
            this.testButtonEnabled = testButtonEnabled;
            return this;
        }

        public Builder withAutomaticSummerTimingEnabled(Boolean automaticSummerTimingEnabled) {
            this.automaticSummerTimingEnabled = automaticSummerTimingEnabled;
            return this;
        }

        public Builder withAstroGateSunRiseOffset(Integer astroGateSunRiseOffset) {
            this.astroGateSunRiseOffset = astroGateSunRiseOffset;
            return this;
        }

        public Builder withAstroGateSunSetOffset(Integer astroGateSunSetOffset) {
            this.astroGateSunSetOffset = astroGateSunSetOffset;
            return this;
        }

        public Builder withSwitchingDelays(List<Integer> switchingDelays) {
            this.switchingDelays = switchingDelays;
            return this;
        }

        public Builder withRelayLinking(List<RelayMatrix> relayLinking) {
            this.relayLinking = relayLinking;
            return this;
        }

        public Builder withRelayRefreshing(Boolean relayRefreshing) {
            this.relayRefreshing = relayRefreshing;
            return this;
        }

        public Builder withSummerTimeDetails(DateTime summerTimeDetails) {
            this.summerTimeDetails = summerTimeDetails;
            return this;
        }

        public Builder withWinterTimeDetails(DateTime winterTimeDetails) {
            this.winterTimeDetails = winterTimeDetails;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
