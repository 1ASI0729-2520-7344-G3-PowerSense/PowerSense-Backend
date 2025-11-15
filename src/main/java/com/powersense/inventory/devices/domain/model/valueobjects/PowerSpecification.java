package com.powersense.inventory.devices.domain.model.valueobjects;

public record PowerSpecification(int watts, Integer voltage, Integer amperage) {
    public PowerSpecification {
        if (watts <= 0) {
            throw new IllegalArgumentException("PowerSpecification.watts must be > 0");
        }
    }
}
