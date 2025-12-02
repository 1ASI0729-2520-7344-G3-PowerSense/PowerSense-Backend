package com.powersense.inventory.devices.application.internal.outboundservices.repositories;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    Optional<Device> findById(DeviceId id);

    List<Device> findAll();

    List<Device> findByRoomId(RoomId roomId);

    Device save(Device device);

    void saveAll(List<Device> devices);

    void deleteById(DeviceId id);

    DeviceId nextIdentity();

    List<Device> findAllByUserId(Long userId);
}
