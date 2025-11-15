package com.powersense.inventory.devices.domain.model.commands;

public record SetDeviceStatus(
        String deviceId,
        String status
) {}
