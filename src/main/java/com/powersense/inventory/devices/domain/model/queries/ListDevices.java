package com.powersense.inventory.devices.domain.model.queries;

public record ListDevices(
        String search,
        String category,
        String roomId,
        String status
) {
    public ListDevices() {
        this(null, null, null, null);
    }
}
