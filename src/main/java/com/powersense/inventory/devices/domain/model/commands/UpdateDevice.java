package com.powersense.inventory.devices.domain.model.commands;

public record UpdateDevice(
        String deviceId,
        String name,
        String category,
        String roomId,
        String roomName,
        Integer watts
) {}
