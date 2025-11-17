package com.powersense.inventory.devices.application.internal.commandservices;

import com.powersense.inventory.devices.application.internal.eventbus.EventBus;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.exceptions.DeviceNotFoundException;
import com.powersense.inventory.devices.domain.exceptions.InvalidDeviceStatusException;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.events.AllDevicesStatusChanged;
import com.powersense.inventory.devices.domain.model.events.DeviceDeleted;
import com.powersense.inventory.devices.domain.model.events.DeviceUpdated;
import com.powersense.inventory.devices.domain.model.events.RoomDevicesStatusChanged;
import com.powersense.inventory.devices.domain.model.valueobjects.*;
import com.powersense.inventory.devices.domain.model.commands.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DeviceCommandServiceImpl {

    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    // Inyección de dependencias: patrón adecuado para servicios de aplicación.
    public DeviceCommandServiceImpl(DeviceRepository deviceRepository, EventBus eventBus) {
        this.deviceRepository = deviceRepository;
        this.eventBus = eventBus;
    }

    public Device createDevice(CreateDevice command) {
        // Generación de ID siguiendo las reglas del repositorio (DDD).
        DeviceId id = deviceRepository.nextIdentity();

        // Conversión de campos simples a Value Objects (buena práctica).
        DeviceName name = new DeviceName(command.name());
        DeviceCategory category = parseCategory(command.category());
        Location location = new Location(new RoomId(command.roomId()), new RoomName(command.roomName()));

        // Se podría validar información de potencia más adelante.
        PowerSpecification power = new PowerSpecification(command.watts(), null, null);

        // Creación del agregado siguiendo las reglas de fábrica del dominio.
        Device device = Device.create(id, name, category, location, power);

        // Persistencia del agregado.
        deviceRepository.save(device);

        // Publicación de eventos de dominio generados durante la creación.
        eventBus.publish(device.pullDomainEvents());
        return device;
    }

    public Device updateDevice(UpdateDevice command) {
        DeviceId id = new DeviceId(command.deviceId());

        // Recuperación del agregado y manejo explícito de error.
        Device existing = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id.value()));

        // Aplicación condicional de cambios: respeta inmutabilidad de Value Objects.
        DeviceName name = command.name() != null ? new DeviceName(command.name()) : existing.getName();
        DeviceCategory category = command.category() != null ? parseCategory(command.category()) : existing.getCategory();

        // Actualización parcial de ubicación.
        Location location = existing.getLocation();
        if (command.roomId() != null || command.roomName() != null) {
            RoomId roomId = command.roomId() != null ? new RoomId(command.roomId()) : location.roomId();
            RoomName roomName = command.roomName() != null ? new RoomName(command.roomName()) : location.roomName();
            location = new Location(roomId, roomName);
        }

        // Actualización selectiva de potencia.
        PowerSpecification power = existing.getPower();
        if (command.watts() != null) {
            power = new PowerSpecification(command.watts(), power.voltage(), power.amperage());
        }

        // Creación de un nuevo agregado para mantener inmutabilidad.
        Device updated = new Device(existing.getId(), name, category, existing.getStatus(), location, power);

        // Persistencia del dispositivo actualizado.
        deviceRepository.save(updated);

        // Publicación explícita del evento DeviceUpdated.
        eventBus.publish(new DeviceUpdated(id.value(), Instant.now()));
        return updated;
    }

    public void deleteDevice(DeleteDevice command) {
        DeviceId id = new DeviceId(command.deviceId());

        // No se valida si existe antes de eliminar: se podría mejorar.
        deviceRepository.deleteById(id);

        // Evento de eliminación.
        eventBus.publish(new DeviceDeleted(id.value(), Instant.now()));
    }

    public Device setDeviceStatus(SetDeviceStatus command) {
        DeviceId id = new DeviceId(command.deviceId());

        // Obtención del dispositivo o error si no existe.
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id.value()));

        // Conversión de estado desde string a Value Object.
        DeviceStatus targetStatus = parseStatus(command.status());

        // Reglas específicas del dominio.
        if (targetStatus.isActive()) {
            device.activate();
        } else {
            device.deactivate();
        }

        // Persistencia y publicación de los eventos generados.
        deviceRepository.save(device);
        eventBus.publish(device.pullDomainEvents());
        return device;
    }

    public void setRoomDevicesStatus(SetRoomDevicesStatus command) {
        RoomId roomId = new RoomId(command.roomId());
        DeviceStatus targetStatus = parseStatus(command.status());

        // Obtención de todos los dispositivos de un cuarto.
        List<Device> devices = deviceRepository.findByRoomId(roomId);

        int affected = 0;
        List<com.powersense.inventory.devices.domain.model.events.DomainEvent> collected = new ArrayList<>();

        for (Device d : devices) {
            boolean wasActive = d.isActive();

            // Activación/desactivación en lote basada en el estado objetivo.
            if (targetStatus.isActive()) {
                d.activate();
            } else {
                d.deactivate();
            }

            // Conteo de cambios reales para estadísticas del dominio.
            if (wasActive != d.isActive()) {
                affected++;
            }

            // Recolecta eventos de dominio generados por cada dispositivo.
            collected.addAll(d.pullDomainEvents());
        }

        // Persistencia en lote para eficiencia.
        deviceRepository.saveAll(devices);

        // Publicación de todos los eventos acumulados.
        eventBus.publish(collected);

        // Se obtiene un nombre de cuarto "best effort".
        String roomName = devices.isEmpty() ? "" : devices.get(0).getLocation().roomName().value();

        // Evento específico para cambios a nivel de cuarto.
        eventBus.publish(new RoomDevicesStatusChanged(
                roomId.value(),
                roomName,
                targetStatus.value(),
                affected,
                Instant.now()
        ));
    }

    public void setAllDevicesStatus(SetAllDevicesStatus command) {
        DeviceStatus targetStatus = parseStatus(command.status());
        List<Device> devices = deviceRepository.findAll();

        int affected = 0;
        List<com.powersense.inventory.devices.domain.model.events.DomainEvent> collected = new ArrayList<>();

        for (Device d : devices) {
            boolean wasActive = d.isActive();

            // Aplicación global de estado a todos los dispositivos.
            if (targetStatus.isActive()) {
                d.activate();
            } else {
                d.deactivate();
            }

            if (wasActive != d.isActive()) {
                affected++;
            }

            collected.addAll(d.pullDomainEvents());
        }

        deviceRepository.saveAll(devices);

        // Publicación de cada evento generado.
        eventBus.publish(collected);

        // Evento global para registrar el cambio masivo.
        eventBus.publish(new AllDevicesStatusChanged(targetStatus.value(), devices.size(), Instant.now()));
    }

    public void importDevices(ImportDevices command) {
        List<Device> toSave = new ArrayList<>();
        List<com.powersense.inventory.devices.domain.model.events.DomainEvent> collected = new ArrayList<>();

        // Importación masiva de dispositivos desde datos externos.
        for (ImportDevices.DeviceData d : command.devices()) {
            DeviceId id = deviceRepository.nextIdentity();
            DeviceName name = new DeviceName(d.name());
            DeviceCategory category = parseCategory(d.category());
            Location location = new Location(new RoomId(d.roomId()), new RoomName(d.roomName()));
            PowerSpecification power = new PowerSpecification(d.watts(), null, null);

            // Creación de cada dispositivo usando reglas del dominio.
            Device device = Device.create(id, name, category, location, power);

            collected.addAll(device.pullDomainEvents());
            toSave.add(device);
        }

        deviceRepository.saveAll(toSave);

        // Publicación conjunta de eventos de importación.
        eventBus.publish(collected);
        eventBus.publish(new com.powersense.inventory.devices.domain.model.events.DevicesImported(toSave.size(), Instant.now()));
    }

    private DeviceCategory parseCategory(String raw) {
        // Conversión de String a categoría, con fallback por defecto.
        if (raw == null) return DeviceCategory.GENERIC_POWER;
        String val = raw.trim();
        for (DeviceCategory c : DeviceCategory.values()) {
            if (c.name().equalsIgnoreCase(val) || c.value().equalsIgnoreCase(val)) {
                return c;
            }
        }
        return DeviceCategory.GENERIC_POWER;
    }

    private DeviceStatus parseStatus(String raw) {
        // Validación estricta del estado: evita silencios de error.
        if (raw == null) {
            throw new InvalidDeviceStatusException("Status must be 'active' or 'inactive'");
        }
        String v = raw.trim().toLowerCase(Locale.ROOT);

        // Mapeo de estados válido → objeto de dominio.
        return switch (v) {
            case "active" -> DeviceStatus.ACTIVE;
            case "inactive" -> DeviceStatus.INACTIVE;
            default -> throw new InvalidDeviceStatusException("Invalid status: " + raw);
        };
    }
}
