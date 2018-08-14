package org.opensmartgridplatform.adapter.ws.core.application.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.opensmartgridplatform.adapter.ws.schema.core.deviceinstallation.Device;
import org.opensmartgridplatform.domain.core.entities.Ssld;

public class DeviceInstallationMapperTest {

    private final static String DEVICE_IDENTIFICATION = "device_identification";
    private final static String ALIAS = "alias";
    private final static String CITY = "city";
    private final static String POSTAL_CODE = "postal_code";
    private final static String STREET = "street";
    private final static String NUMBER = "number";
    private final static String MUNICIPALITY = "municipality";
    private final static Float GPS_LATITUDE = 50f;
    private final static Float GPS_LONGITUDE = 5f;

    DeviceInstallationMapper mapper = new DeviceInstallationMapper();

    @Before
    public void setup() {
        this.mapper.initialize();
    }

    @Test
    public void shouldConvertWsDeviceToSsld() {
        // given
        final Device device = this.createDevice();

        // when
        final Ssld ssld = this.mapper.map(device, Ssld.class);

        // then
        assertThat(ssld.getContainer()).isNotNull();
        assertThat(ssld.getContainer().getCity()).isEqualTo(CITY);
        assertThat(ssld.getContainer().getPostalCode()).isEqualTo(POSTAL_CODE);
    }

    private Device createDevice() {
        final Device device = new Device();
        device.setDeviceIdentification(DEVICE_IDENTIFICATION);
        device.setAlias(ALIAS);
        device.setContainerCity(CITY);
        device.setContainerPostalCode(POSTAL_CODE);
        device.setContainerStreet(STREET);
        device.setContainerNumber(NUMBER);
        device.setContainerMunicipality(MUNICIPALITY);
        device.setGpsLatitude(GPS_LATITUDE);
        device.setGpsLongitude(GPS_LONGITUDE);
        return device;
    }
}
