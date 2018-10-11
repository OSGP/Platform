package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.Configuration;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.DaliConfiguration;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.DeviceFixedIp;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.IndexAddressMap;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.LightType;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.LinkType;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.LongTermIntervalType;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.MeterType;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.RelayConfiguration;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.RelayMap;
import org.opensmartgridplatform.adapter.ws.schema.core.configurationmanagement.RelayMatrix;

public class ConfigurationManagementMapperTest {
    private ConfigurationManagementMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ConfigurationManagementMapper();
        mapper.configure(new DefaultMapperFactory.Builder().build());
    }

    @Test
    public void mapsWsConfigurationToDomainConfiguration() {
        final Configuration source = new Configuration();
        source.setLightType(LightType.DALI);
        source.setDaliConfiguration(aSourceDaliConfiguration(123, 124, 125, 126, 127));
        source.setRelayConfiguration(aSourceRelayConfiguration(128, 129, 130, 131));
        source.setShortTermHistoryIntervalMinutes(132);
        source.setPreferredLinkType(LinkType.CDMA);
        source.setMeterType(MeterType.AUX);
        source.setLongTermHistoryInterval(133);
        source.setLongTermHistoryIntervalType(LongTermIntervalType.DAYS);
        source.setTimeSyncFrequency(134);
        source.setDeviceFixedIp(aSourceDeviceFixedIp("ipAddress1", "netMask1", "gateWay1"));
        source.setDhcpEnabled(true);
        source.setCommunicationTimeout(135);
        source.setCommunicationNumberOfRetries(136);
        source.setCommunicationPauseTimeBetweenConnectionTrials(137);
        source.setOsgpIpAddress("osgpIpAddress1");
        source.setOsgpPortNumber(138);
        source.setNtpHost("ntpHost1");
        source.setNtpEnabled(true);
        source.setNtpSyncInterval(139);
        source.setTestButtonEnabled(true);
        source.setAutomaticSummerTimingEnabled(true);
        source.setAstroGateSunRiseOffset(140);
        source.setAstroGateSunSetOffset(141);
        source.getSwitchingDelays().addAll(asList(142, 142));
        source.getRelayLinking().addAll(asList(aSourceRelayMatrix(143, true), aSourceRelayMatrix(144, false)));
        source.setSummerTimeDetails(XMLGregorianCalendarImpl.parse("2010-06-30T01:20:30+02:00"));
        source.setWinterTimeDetails(XMLGregorianCalendarImpl.parse("2011-06-30T01:20:30+02:00"));
        source.setRelayRefreshing(true);

        final org.opensmartgridplatform.domain.core.valueobjects.Configuration result = mapper.map(source,
                org.opensmartgridplatform.domain.core.valueobjects.Configuration.class);

        final org.opensmartgridplatform.domain.core.valueobjects.Configuration expected =
                new org.opensmartgridplatform.domain.core.valueobjects.Configuration.Builder()
                        .withLightType(org.opensmartgridplatform.domain.core.valueobjects.LightType.DALI)
                        .withDaliConfiguration(aTargetDaliConfiguration(123, 124, 125, 126, 127))
                        .withRelayConfiguration(aTargetRelayConfiguration(128, 129, 130, 131))
                        .withShortTemHistoryIntervalMinutes(132)
                        .withPreferredLinkType(org.opensmartgridplatform.domain.core.valueobjects.LinkType.CDMA)
                        .withMeterType(org.opensmartgridplatform.domain.core.valueobjects.MeterType.AUX)
                        .withLongTermHistoryInterval(133)
                        .withLongTermHistoryIntervalType(
                                org.opensmartgridplatform.domain.core.valueobjects.LongTermIntervalType.DAYS)
                        .withTimeSyncFrequency(134)
                        .withDeviceFixedIp(new org.opensmartgridplatform.domain.core.valueobjects.DeviceFixedIp(
                                "ipAddress1", "netMask1", "gateWay1"))
                        .withDhcpEnabled(true)
                        .withCommunicationTimeout(135)
                        .withCommunicationNumberOfRetries(136)
                        .withCommunicationPauseTimeBetweenConnectionTrials(137)
                        .withOsgpIpAddress("osgpIpAddress1")
                        .withOsgpPortNumber(138)
                        .withNtpHost("ntpHost1")
                        .withNtpEnabled(true)
                        .withNtpSyncInterval(139)
                        .withTestButtonEnabled(true)
                        .withAutomaticSummerTimingEnabled(true)
                        .withAstroGateSunRiseOffset(140)
                        .withAstroGateSunSetOffset(141)
                        .withSwitchingDelays(asList(142, 142))
                        .withRelayLinking(asList(aTargetRelayMatrix(143, true), aTargetRelayMatrix(144, false)))
                        .withRelayRefreshing(true)
                        .withSummerTimeDetails(DateTime.parse("2010-06-30T01:20:30+02:00"))
                        .withWinterTimeDetails(DateTime.parse("2011-06-30T01:20:30+02:00"))
                        .build();
        assertThat(result).isEqualToComparingFieldByFieldRecursively(expected);
    }

    private org.opensmartgridplatform.domain.core.valueobjects.RelayMatrix aTargetRelayMatrix(int masterRelayIndex,
            boolean masterRelayOn) {
        org.opensmartgridplatform.domain.core.valueobjects.RelayMatrix relayMatrix =
                new org.opensmartgridplatform.domain.core.valueobjects.RelayMatrix(masterRelayIndex, masterRelayOn);
        relayMatrix.setIndicesOfControlledRelaysOff(emptyList());
        relayMatrix.setIndicesOfControlledRelaysOn(emptyList());
        return relayMatrix;
    }

    private RelayMatrix aSourceRelayMatrix(int masterRelayIndex, boolean masterRelayOn) {
        RelayMatrix relayMatrix = new RelayMatrix();
        relayMatrix.setMasterRelayIndex(masterRelayIndex);
        relayMatrix.setMasterRelayOn(masterRelayOn);
        return relayMatrix;
    }

    private DeviceFixedIp aSourceDeviceFixedIp(String ipAddress, String netMask, String gateWay) {
        DeviceFixedIp deviceFixedIp = new DeviceFixedIp();
        deviceFixedIp.setGateWay(gateWay);
        deviceFixedIp.setIpAddress(ipAddress);
        deviceFixedIp.setNetMask(netMask);
        return deviceFixedIp;
    }

    private org.opensmartgridplatform.domain.core.valueobjects.RelayConfiguration aTargetRelayConfiguration(int index1,
            int address1, int index2, int address2) {
        return new org.opensmartgridplatform.domain.core.valueobjects.RelayConfiguration(asList(
                new org.opensmartgridplatform.domain.core.valueobjects.RelayMap(index1, address1, null, null),
                new org.opensmartgridplatform.domain.core.valueobjects.RelayMap(index2, address2, null, null)));
    }

    private RelayConfiguration aSourceRelayConfiguration(int index1, int address1, int index2, int address2) {
        RelayConfiguration relayConfiguration = new RelayConfiguration();
        relayConfiguration.getRelayMap().add(aSourceRelayMap(index1, address1));
        relayConfiguration.getRelayMap().add(aSourceRelayMap(index2, address2));
        return relayConfiguration;
    }

    private RelayMap aSourceRelayMap(int index, int address) {
        RelayMap relayMap = new RelayMap();
        relayMap.setIndex(index);
        relayMap.setAddress(address);
        return relayMap;
    }

    private org.opensmartgridplatform.domain.core.valueobjects.DaliConfiguration aTargetDaliConfiguration(int numberOfLights,
            int index1, int address1, int index2, int address2) {
        HashMap<Integer, Integer> indexAddressMap = new HashMap<>();
        indexAddressMap.put(index1, address1);
        indexAddressMap.put(index2, address2);
        return new org.opensmartgridplatform.domain.core.valueobjects.DaliConfiguration(numberOfLights,
                indexAddressMap
        );
    }

    private DaliConfiguration aSourceDaliConfiguration(int numberOfLights, int index1, int address1, int index2, int address2) {
        DaliConfiguration daliConfiguration = new DaliConfiguration();
        daliConfiguration.setNumberOfLights(numberOfLights);
        daliConfiguration.getIndexAddressMap().add(aSourceIndexAddressMap(index1, address1));
        daliConfiguration.getIndexAddressMap().add(aSourceIndexAddressMap(index2, address2));
        return daliConfiguration;
    }

    private IndexAddressMap aSourceIndexAddressMap(final int index, final int address) {
        IndexAddressMap indexAddressMap = new IndexAddressMap();
        indexAddressMap.setIndex(index);
        indexAddressMap.setAddress(address);
        return indexAddressMap;
    }
}