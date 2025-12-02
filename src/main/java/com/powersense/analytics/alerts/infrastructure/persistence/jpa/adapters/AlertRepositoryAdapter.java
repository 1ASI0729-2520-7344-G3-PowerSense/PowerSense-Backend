package com.powersense.analytics.alerts.infrastructure.persistence.jpa.adapters;

import com.powersense.analytics.alerts.application.internal.outboundservices.repositories.AlertRepository;
import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import com.powersense.analytics.alerts.infrastructure.persistence.jpa.entities.AlertEntity;
import com.powersense.analytics.alerts.infrastructure.persistence.jpa.repositories.AlertJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AlertRepositoryAdapter implements AlertRepository {

    private final AlertJpaRepository jpaRepository;

    public AlertRepositoryAdapter(AlertJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Alert> findById(AlertId id) {
        return jpaRepository.findById(id.value()).map(AlertEntity::toDomain);
    }

    @Override
    public List<Alert> findAll() {
        return jpaRepository.findAll().stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findByType(AlertType type) {
        return jpaRepository.findByType(type).stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findBySeverity(AlertSeverity severity) {
        return jpaRepository.findBySeverity(severity).stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findByDeviceId(String deviceId) {
        return jpaRepository.findByDeviceId(deviceId).stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Alert> findByAcknowledged(boolean acknowledged) {
        return jpaRepository.findByAcknowledged(acknowledged).stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Alert save(Alert alert) {
        AlertEntity entity = AlertEntity.fromDomain(alert);
        AlertEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public void deleteById(AlertId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public AlertId nextIdentity() {
        return new AlertId(UUID.randomUUID().toString());
    }

    @Override
    public List<Alert> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(AlertEntity::toDomain)
                .collect(Collectors.toList());
    }
}
