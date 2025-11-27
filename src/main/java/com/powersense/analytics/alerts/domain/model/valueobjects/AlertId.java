package com.powersense.analytics.alerts.domain.model.valueobjects;

import java.util.Objects;

public record AlertId(String value) {
    public AlertId {
        Objects.requireNonNull(value, "Alert ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Alert ID cannot be empty");
        }
    }
}
