package com.powersense.inventory.devices.domain.model.commands;

import java.util.List;

public record ImportDevices(List<DeviceData> devices) {
    public static record DeviceData(
            String name,
            String category,
            String roomId,
            String roomName,
            int watts
    ) {}
}
