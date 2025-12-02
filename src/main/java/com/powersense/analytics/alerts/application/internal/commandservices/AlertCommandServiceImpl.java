package com.powersense.analytics.alerts.application.internal.commandservices;

import com.powersense.analytics.alerts.application.internal.outboundservices.repositories.AlertRepository;
import com.powersense.analytics.alerts.domain.exceptions.AlertNotFoundException;
import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.commands.AcknowledgeAlert;
import com.powersense.analytics.alerts.domain.model.commands.CreateAlert;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import com.powersense.auth.infrastructure.tokens.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlertCommandServiceImpl {

    private final AlertRepository alertRepository;
    private final JwtTokenService jwtTokenService;

    public AlertCommandServiceImpl(AlertRepository alertRepository, JwtTokenService jwtTokenService) {
        this.alertRepository = alertRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public Alert createAlert(CreateAlert command) {
        Long userId = getCurrentUserId();
        AlertId id = alertRepository.nextIdentity();
        AlertType type = AlertType.valueOf(command.type().toUpperCase());
        AlertSeverity severity = AlertSeverity.valueOf(command.severity().toUpperCase());

        Alert alert = Alert.create(id, type, severity, command.deviceId(),
                command.threshold(), command.message(), userId);

        return alertRepository.save(alert);
    }

    public Alert acknowledgeAlert(AcknowledgeAlert command) {
        AlertId id = new AlertId(command.alertId());
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(command.alertId()));

        alert.acknowledge();

        return alertRepository.save(alert);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String token = (String) authentication.getCredentials();
        if (token == null) {
            return null;
        }
        return jwtTokenService.extractUserId(token);
    }
}
