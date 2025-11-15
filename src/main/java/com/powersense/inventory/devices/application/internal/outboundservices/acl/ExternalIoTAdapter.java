package com.powersense.inventory.devices.application.internal.outboundservices.acl;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import org.springframework.stereotype.Component;

@Component
public class ExternalIoTAdapter {

    public void sendActivationSignal(String deviceId) {
        System.out.println("[ExternalIoTAdapter] sendActivationSignal for device " + deviceId + " (mock)");
    }

    public void sendDeactivationSignal(String deviceId) {
        System.out.println("[ExternalIoTAdapter] sendDeactivationSignal for device " + deviceId + " (mock)");
    }

    public DeviceStatus readCurrentStatus(String deviceId) {
        System.out.println("[ExternalIoTAdapter] readCurrentStatus for device " + deviceId + " (mock -> INACTIVE)");
        return DeviceStatus.INACTIVE;
    }
}
