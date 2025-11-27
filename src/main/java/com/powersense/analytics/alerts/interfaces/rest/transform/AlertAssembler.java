package com.powersense.analytics.alerts.interfaces.rest.transform;

import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.commands.CreateAlert;
import com.powersense.analytics.alerts.interfaces.rest.resources.AlertResponse;
import com.powersense.analytics.alerts.interfaces.rest.resources.CreateAlertResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertAssembler {

    public AlertResponse toResponse(Alert alert) {
        AlertResponse response = new AlertResponse();
        response.setId(alert.getId().value());
        response.setType(alert.getType().name());
        response.setSeverity(alert.getSeverity().name());
        response.setDeviceId(alert.getDeviceId());
        response.setThreshold(alert.getThreshold());
        response.setMessage(alert.getMessage());
        response.setAcknowledged(alert.isAcknowledged());
        response.setAcknowledgedAt(alert.getAcknowledgedAt());
        response.setCreatedAt(alert.getCreatedAt());
        response.setUpdatedAt(alert.getUpdatedAt());
        return response;
    }

    public List<AlertResponse> toResponseList(List<Alert> alerts) {
        return alerts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CreateAlert toCreateCommand(CreateAlertResource resource) {
        return new CreateAlert(
                resource.getType(),
                resource.getSeverity(),
                resource.getDeviceId(),
                resource.getThreshold(),
                resource.getMessage()
        );
    }
}
