package com.powersense.inventory.devices.domain.model.aggregates;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Room {
    private final RoomId id;
    private final RoomName name;
    private final List<DeviceId> deviceIds = new ArrayList<>();

    public Room(RoomId id, RoomName name) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
    }

    public void addDevice(DeviceId deviceId) {
        Objects.requireNonNull(deviceId, "deviceId");
        if (!deviceIds.contains(deviceId)) {
            deviceIds.add(deviceId);
        }
    }

    public void removeDevice(DeviceId deviceId) {
        Objects.requireNonNull(deviceId, "deviceId");
        deviceIds.remove(deviceId);
    }

    public List<DeviceId> getDeviceIds() {
        return Collections.unmodifiableList(deviceIds);
    }

    public RoomId getId() {
        return id;
    }

    public RoomName getName() {
        return name;
    }
}
