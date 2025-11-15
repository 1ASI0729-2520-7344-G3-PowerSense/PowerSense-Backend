package com.powersense.inventory.devices.domain.model.commands;

public record CreateDevice(
        String name,
        String category,
        String roomId,
        String roomName,
        int watts
) {}
