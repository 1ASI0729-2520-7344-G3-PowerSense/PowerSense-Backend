package com.powersense.analytics.alerts.domain.exceptions;

public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(String alertId) {
        super("Alert not found: " + alertId);
    }
}
