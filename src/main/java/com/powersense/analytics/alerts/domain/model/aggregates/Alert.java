package com.powersense.analytics.alerts.domain.model.aggregates;

import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;

import java.time.Instant;
import java.util.Objects;

public class Alert {
    private final AlertId id;
    private final AlertType type;
    private final AlertSeverity severity;
    private String deviceId;
    private Double threshold;
    private String message;
    private boolean acknowledged;
    private Instant acknowledgedAt;
    private Long userId;
    private final Instant createdAt;
    private Instant updatedAt;

    public Alert(AlertId id, AlertType type, AlertSeverity severity, String deviceId,
            Double threshold, String message, Long userId) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
        this.severity = Objects.requireNonNull(severity, "severity");
        this.deviceId = deviceId;
        this.threshold = threshold;
        this.message = Objects.requireNonNull(message, "message");
        this.acknowledged = false;
        this.acknowledgedAt = null;
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static Alert create(AlertId id, AlertType type, AlertSeverity severity,
            String deviceId, Double threshold, String message, Long userId) {
        return new Alert(id, type, severity, deviceId, threshold, message, userId);
    }

    public void acknowledge() {
        if (!this.acknowledged) {
            this.acknowledged = true;
            this.acknowledgedAt = Instant.now();
            this.updatedAt = Instant.now();
        }
    }

    public void unacknowledge() {
        if (this.acknowledged) {
            this.acknowledged = false;
            this.acknowledgedAt = null;
            this.updatedAt = Instant.now();
        }
    }

    // Getters
    public AlertId getId() {
        return id;
    }

    public AlertType getType() {
        return type;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Double getThreshold() {
        return threshold;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getUserId() {
        return userId;
    }
}
