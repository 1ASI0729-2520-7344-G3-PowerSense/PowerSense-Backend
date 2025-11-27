package com.powersense.analytics.alerts.application.internal.outboundservices.repositories;

import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    Optional<Alert> findById(AlertId id);
    List<Alert> findAll();
    List<Alert> findByType(AlertType type);
    List<Alert> findBySeverity(AlertSeverity severity);
    List<Alert> findByDeviceId(String deviceId);
    List<Alert> findByAcknowledged(boolean acknowledged);
    Alert save(Alert alert);
    void deleteById(AlertId id);
    AlertId nextIdentity();
}
