package com.powersense.inventory.devices.infrastructure.persistence.inmemory;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDeviceRepository implements DeviceRepository {

    private final Map<String, Device> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Device> findById(DeviceId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public List<Device> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Device> findByRoomId(RoomId roomId) {
        return storage.values().stream()
                .filter(d -> d.getLocation().roomId().value().equals(roomId.value()))
                .toList();
    }

    @Override
    public Device save(Device device) {
        storage.put(device.getId().value(), device);
        return device;
    }

    @Override
    public void saveAll(List<Device> devices) {
        for (Device d : devices) storage.put(d.getId().value(), d);
    }

    @Override
    public void deleteById(DeviceId id) {
        storage.remove(id.value());
    }

    @Override
    public DeviceId nextIdentity() {
        return new DeviceId("dev-" + UUID.randomUUID());
    }
}

