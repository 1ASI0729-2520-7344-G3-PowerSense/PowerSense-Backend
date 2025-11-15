package com.powersense.inventory.devices.domain.model.commands;

public record SetRoomDevicesStatus(
        String roomId,
        String status
) {}
