package com.alliander.osgp.domain.microgrids.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alliander.osgp.domain.microgrids.entities.RtuDevice;

@Repository
public interface RtuDeviceRepository extends JpaRepository<RtuDevice, Long> {
    RtuDevice findByDeviceIdentification(String deviceIdentification);
}
