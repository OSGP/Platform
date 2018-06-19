package com.alliander.osgp.core.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.domain.core.entities.Device;
import com.alliander.osgp.domain.core.entities.DomainInfo;
import com.alliander.osgp.domain.core.entities.Event;
import com.alliander.osgp.domain.core.entities.Ssld;
import com.alliander.osgp.domain.core.exceptions.UnknownEntityException;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.DomainInfoRepository;
import com.alliander.osgp.domain.core.repositories.EventRepository;
import com.alliander.osgp.domain.core.repositories.SsldRepository;

@Service
public class EventNotificationHelperService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SsldRepository ssldRepository;

    @Autowired
    private DomainInfoRepository domainInfoRepository;

    public Device findDevice(final String deviceIdentification) throws UnknownEntityException {
        final Device device = this.deviceRepository.findByDeviceIdentification(deviceIdentification);
        if (device == null) {
            throw new UnknownEntityException(Device.class, deviceIdentification);
        }
        return device;
    }

    @Transactional(value = "transactionManager")
    public Device saveDevice(final Device device) {
        return this.deviceRepository.save(device);
    }

    public Ssld findSsld(final Long id) throws UnknownEntityException {
        final Ssld ssld = this.ssldRepository.findOne(id);
        if (ssld == null) {
            throw new UnknownEntityException(Ssld.class, Long.toString(id));
        }
        return ssld;
    }

    @Transactional(value = "transactionManager")
    public Ssld saveSsld(final Ssld ssld) {
        return this.ssldRepository.save(ssld);
    }

    @Transactional(value = "transactionManager")
    public Event saveEvent(final Event event) {
        return this.eventRepository.save(event);
    }

    public DomainInfo findDomainInfo(final String domainName, final String domainVersion) {
        return this.domainInfoRepository.findByDomainAndDomainVersion(domainName, domainVersion);
    }

}
