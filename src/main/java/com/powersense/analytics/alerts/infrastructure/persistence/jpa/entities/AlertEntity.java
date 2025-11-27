package com.powersense.analytics.alerts.infrastructure.persistence.jpa.entities;

import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "alerts")
public class AlertEntity {
    @Id
    @Column(length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AlertSeverity severity;

    @Column(length = 36)
    private String deviceId;

    @Column
    private Double threshold;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean acknowledged = false;

    @Column
    private Instant acknowledgedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public AlertEntity() {
    }

    public AlertEntity(String id, AlertType type, AlertSeverity severity, String deviceId,
                       Double threshold, String message, boolean acknowledged, Instant acknowledgedAt,
                       Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.deviceId = deviceId;
        this.threshold = threshold;
        this.message = message;
        this.acknowledged = acknowledged;
        this.acknowledgedAt = acknowledgedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AlertEntity fromDomain(Alert alert) {
        return new AlertEntity(
                alert.getId().value(),
                alert.getType(),
                alert.getSeverity(),
                alert.getDeviceId(),
                alert.getThreshold(),
                alert.getMessage(),
                alert.isAcknowledged(),
                alert.getAcknowledgedAt(),
                alert.getCreatedAt(),
                alert.getUpdatedAt()
        );
    }

    public Alert toDomain() {
        Alert alert = new Alert(
                new AlertId(id),
                type,
                severity,
                deviceId,
                threshold,
                message
        );
        
        if (acknowledged) {
            alert.acknowledge();
        }
        
        return alert;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
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

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(Instant acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
