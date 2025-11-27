package com.powersense.analytics.alerts.domain.model.commands;

public record CreateAlert(
        String type,
        String severity,
        String deviceId,
        Double threshold,
        String message
) {}
