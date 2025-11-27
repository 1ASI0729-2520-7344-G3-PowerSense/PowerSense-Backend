package com.powersense.analytics.alerts.application.internal.commandservices;

import com.powersense.analytics.alerts.application.internal.outboundservices.repositories.AlertRepository;
import com.powersense.analytics.alerts.domain.exceptions.AlertNotFoundException;
import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.commands.AcknowledgeAlert;
import com.powersense.analytics.alerts.domain.model.commands.CreateAlert;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlertCommandServiceImpl {

    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert createAlert(CreateAlert command) {
        AlertId id = alertRepository.nextIdentity();
        AlertType type = AlertType.valueOf(command.type().toUpperCase());
        AlertSeverity severity = AlertSeverity.valueOf(command.severity().toUpperCase());

        Alert alert = Alert.create(id, type, severity, command.deviceId(), 
                                   command.threshold(), command.message());

        return alertRepository.save(alert);
    }

    public Alert acknowledgeAlert(AcknowledgeAlert command) {
        AlertId id = new AlertId(command.alertId());
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(command.alertId()));

        alert.acknowledge();

        return alertRepository.save(alert);
    }
}
