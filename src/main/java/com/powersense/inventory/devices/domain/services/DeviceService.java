package com.powersense.inventory.devices.domain.services;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;

public interface DeviceService {
    void validateDeviceCanBeActivated(Device device);
    boolean canDeleteDevice(DeviceId deviceId);
}
