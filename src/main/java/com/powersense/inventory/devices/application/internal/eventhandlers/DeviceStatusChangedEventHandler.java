package com.powersense.inventory.devices.application.internal.eventhandlers;

import com.powersense.inventory.devices.application.internal.outboundservices.acl.ExternalIoTAdapter;
import com.powersense.inventory.devices.domain.model.events.DeviceStatusChanged;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeviceStatusChangedEventHandler {

    private final ExternalIoTAdapter externalIoTAdapter;

    public DeviceStatusChangedEventHandler(ExternalIoTAdapter externalIoTAdapter) {
        this.externalIoTAdapter = externalIoTAdapter;
    }

    @EventListener
    public void handle(DeviceStatusChanged event) {
        System.out.println("[DeviceStatusChangedEventHandler] Device " + event.deviceId() +
                " changed from " + event.previousStatus() + " to " + event.newStatus());

        if ("active".equalsIgnoreCase(event.newStatus())) {
            externalIoTAdapter.sendActivationSignal(event.deviceId());
        } else if ("inactive".equalsIgnoreCase(event.newStatus())) {
            externalIoTAdapter.sendDeactivationSignal(event.deviceId());
        }
    }
}
