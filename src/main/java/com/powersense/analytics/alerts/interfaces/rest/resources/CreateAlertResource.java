package com.powersense.analytics.alerts.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAlertResource {
    @NotBlank(message = "Type cannot be empty")
    private String type; // HIGH_CONSUMPTION, LOW_EFFICIENCY, etc.

    @NotBlank(message = "Severity cannot be empty")
    private String severity; // INFO, WARNING, ERROR, CRITICAL

    private String deviceId;

    @NotNull(message = "Threshold cannot be null")
    private Double threshold;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
