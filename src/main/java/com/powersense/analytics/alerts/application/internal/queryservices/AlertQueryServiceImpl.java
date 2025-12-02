package com.powersense.analytics.alerts.application.internal.queryservices;

import com.powersense.analytics.alerts.application.internal.outboundservices.repositories.AlertRepository;
import com.powersense.analytics.alerts.domain.exceptions.AlertNotFoundException;
import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.queries.GetAlertById;
import com.powersense.analytics.alerts.domain.model.queries.ListAlerts;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertId;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertSeverity;
import com.powersense.analytics.alerts.domain.model.valueobjects.AlertType;
import com.powersense.auth.infrastructure.tokens.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertQueryServiceImpl {

    private final AlertRepository alertRepository;
    private final JwtTokenService jwtTokenService;

    public AlertQueryServiceImpl(AlertRepository alertRepository, JwtTokenService jwtTokenService) {
        this.alertRepository = alertRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public List<Alert> listAlerts(ListAlerts query) {
        Long userId = getCurrentUserId();
        List<Alert> alerts = userId != null ? alertRepository.findAllByUserId(userId) : alertRepository.findAll();

        if (query.type() != null && !query.type().isBlank()) {
            AlertType type = AlertType.valueOf(query.type().toUpperCase());
            alerts = alerts.stream()
                    .filter(a -> a.getType() == type)
                    .collect(Collectors.toList());
        }

        if (query.severity() != null && !query.severity().isBlank()) {
            AlertSeverity severity = AlertSeverity.valueOf(query.severity().toUpperCase());
            alerts = alerts.stream()
                    .filter(a -> a.getSeverity() == severity)
                    .collect(Collectors.toList());
        }

        if (query.deviceId() != null && !query.deviceId().isBlank()) {
            alerts = alerts.stream()
                    .filter(a -> a.getDeviceId() != null && a.getDeviceId().equals(query.deviceId()))
                    .collect(Collectors.toList());
        }

        if (query.acknowledged() != null) {
            alerts = alerts.stream()
                    .filter(a -> a.isAcknowledged() == query.acknowledged())
                    .collect(Collectors.toList());
        }

        return alerts;
    }

    public Alert getAlertById(GetAlertById query) {
        return alertRepository.findById(new AlertId(query.alertId()))
                .orElseThrow(() -> new AlertNotFoundException(query.alertId()));
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
