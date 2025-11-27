package com.powersense.analytics.alerts.domain.model.queries;

public record ListAlerts(
        String type,
        String severity,
        String deviceId,
        Boolean acknowledged,
        String startDate,
        String endDate
) {}
