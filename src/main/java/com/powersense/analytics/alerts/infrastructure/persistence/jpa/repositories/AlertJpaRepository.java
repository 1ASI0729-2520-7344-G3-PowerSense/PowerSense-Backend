package com.powersense.analytics.alerts.infrastructure.persistence.jpa.repositories;

import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import com.powersense.analytics.alerts.infrastructure.persistence.jpa.entities.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, String> {
    List<AlertEntity> findByType(AlertType type);

    List<AlertEntity> findBySeverity(AlertSeverity severity);

    List<AlertEntity> findByDeviceId(String deviceId);

    List<AlertEntity> findByAcknowledged(boolean acknowledged);

    List<AlertEntity> findAllByUserId(Long userId);
}
