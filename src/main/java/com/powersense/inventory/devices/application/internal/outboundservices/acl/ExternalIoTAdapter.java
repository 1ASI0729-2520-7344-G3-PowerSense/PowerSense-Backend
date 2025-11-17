package com.powersense.inventory.devices.application.internal.outboundservices.acl;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import org.springframework.stereotype.Component;

// Adaptador ACL (Anti-Corruption Layer) para interactuar con servicios IoT externos.
// Su objetivo es aislar el dominio de PowerSens de dependencias o protocolos externos,
// permitiendo reemplazar fácilmente la integración real por mocks o nuevos proveedores IoT.
@Component
public class ExternalIoTAdapter {

    // Envía una señal para activar un dispositivo IoT.
    // Actualmente implementado como mock; en un entorno real enviaría una solicitud a un gateway o broker IoT.
    public void sendActivationSignal(String deviceId) {
        System.out.println("[ExternalIoTAdapter] sendActivationSignal for device " + deviceId + " (mock)");
    }

    // Envía una señal para desactivar un dispositivo IoT.
    // Mantiene la misma estructura que una integración real para facilitar futuras implementaciones.
    public void sendDeactivationSignal(String deviceId) {
        System.out.println("[ExternalIoTAdapter] sendDeactivationSignal for device " + deviceId + " (mock)");
    }

    // Consulta el estado actual del dispositivo directamente en la red IoT externa.
    // Devuelve un estado fijo (mock) para propósitos de desarrollo.
    // En una implementación real podría consultar un microservicio IoT, MQTT, o un API de terceros.
    public DeviceStatus readCurrentStatus(String deviceId) {
        System.out.println("[ExternalIoTAdapter] readCurrentStatus for device " + deviceId + " (mock -> INACTIVE)");
        return DeviceStatus.INACTIVE;
    }
}
