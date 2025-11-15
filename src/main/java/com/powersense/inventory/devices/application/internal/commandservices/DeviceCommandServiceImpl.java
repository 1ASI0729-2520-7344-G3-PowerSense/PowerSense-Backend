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
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceCategory;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceName;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.devices.domain.model.valueobjects.Location;
import com.powersense.inventory.devices.domain.model.valueobjects.PowerSpecification;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomName;
import com.powersense.inventory.devices.domain.model.commands.CreateDevice;
import com.powersense.inventory.devices.domain.model.commands.UpdateDevice;
import com.powersense.inventory.devices.domain.model.commands.DeleteDevice;
import com.powersense.inventory.devices.domain.model.commands.SetDeviceStatus;
import com.powersense.inventory.devices.domain.model.commands.SetRoomDevicesStatus;
import com.powersense.inventory.devices.domain.model.commands.SetAllDevicesStatus;
import com.powersense.inventory.devices.domain.model.commands.ImportDevices;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DeviceCommandServiceImpl {

    private final DeviceRepository deviceRepository;
    private final EventBus eventBus;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository, EventBus eventBus) {
        this.deviceRepository = deviceRepository;
        this.eventBus = eventBus;
    }

    public Device createDevice(CreateDevice command) {
        DeviceId id = deviceRepository.nextIdentity();
        DeviceName name = new DeviceName(command.name());
        DeviceCategory category = parseCategory(command.category());
        Location location = new Location(new RoomId(command.roomId()), new RoomName(command.roomName()));
        PowerSpecification power = new PowerSpecification(command.watts(), null, null);

        Device device = Device.create(id, name, category, location, power);
        deviceRepository.save(device);
        eventBus.publish(device.pullDomainEvents());
        return device;
    }

    public Device updateDevice(UpdateDevice command) {
        DeviceId id = new DeviceId(command.deviceId());
        Device existing = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id.value()));

        DeviceName name = command.name() != null ? new DeviceName(command.name()) : existing.getName();
        DeviceCategory category = command.category() != null ? parseCategory(command.category()) : existing.getCategory();

        Location location = existing.getLocation();
        if (command.roomId() != null || command.roomName() != null) {
            RoomId roomId = command.roomId() != null ? new RoomId(command.roomId()) : location.roomId();
            RoomName roomName = command.roomName() != null ? new RoomName(command.roomName()) : location.roomName();
            location = new Location(roomId, roomName);
        }

        PowerSpecification power = existing.getPower();
        if (command.watts() != null) {
            power = new PowerSpecification(command.watts(), power.voltage(), power.amperage());
        }

        Device updated = new Device(existing.getId(), name, category, existing.getStatus(), location, power);
        deviceRepository.save(updated);
        eventBus.publish(new DeviceUpdated(id.value(), Instant.now()));
        return updated;
    }

    public void deleteDevice(DeleteDevice command) {
        DeviceId id = new DeviceId(command.deviceId());
        deviceRepository.deleteById(id);
        eventBus.publish(new DeviceDeleted(id.value(), Instant.now()));
    }

    public Device setDeviceStatus(SetDeviceStatus command) {
        DeviceId id = new DeviceId(command.deviceId());
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id.value()));

        DeviceStatus targetStatus = parseStatus(command.status());
        if (targetStatus.isActive()) {
            device.activate();
        } else {
            device.deactivate();
        }

        deviceRepository.save(device);
        eventBus.publish(device.pullDomainEvents());
        return device;
    }

    public void setRoomDevicesStatus(SetRoomDevicesStatus command) {
        RoomId roomId = new RoomId(command.roomId());
        DeviceStatus targetStatus = parseStatus(command.status());

        List<Device> devices = deviceRepository.findByRoomId(roomId);
        int affected = 0;
        List<com.powersense.inventory.devices.domain.model.events.DomainEvent> collected = new ArrayList<>();

        for (Device d : devices) {
            boolean wasActive = d.isActive();
            if (targetStatus.isActive()) {
                d.activate();
            } else {
                d.deactivate();
            }
            boolean changed = wasActive != d.isActive();
            if (changed) {
                affected++;
            }
            collected.addAll(d.pullDomainEvents());
        }

        deviceRepository.saveAll(devices);
        eventBus.publish(collected);

        // Best effort room name from first device location (mock)
        String roomName = devices.isEmpty() ? "" : devices.get(0).getLocation().roomName().value();
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
            if (targetStatus.isActive()) {
                d.activate();
            } else {
                d.deactivate();
            }
            boolean changed = wasActive != d.isActive();
            if (changed) {
                affected++;
            }
            collected.addAll(d.pullDomainEvents());
        }

        deviceRepository.saveAll(devices);
        eventBus.publish(collected);
        eventBus.publish(new AllDevicesStatusChanged(targetStatus.value(), devices.size(), Instant.now()));
    }

    public void importDevices(ImportDevices command) {
        List<Device> toSave = new ArrayList<>();
        List<com.powersense.inventory.devices.domain.model.events.DomainEvent> collected = new ArrayList<>();

        for (ImportDevices.DeviceData d : command.devices()) {
            DeviceId id = deviceRepository.nextIdentity();
            DeviceName name = new DeviceName(d.name());
            DeviceCategory category = parseCategory(d.category());
            Location location = new Location(new RoomId(d.roomId()), new RoomName(d.roomName()));
            PowerSpecification power = new PowerSpecification(d.watts(), null, null);
            Device device = Device.create(id, name, category, location, power);
            collected.addAll(device.pullDomainEvents());
            toSave.add(device);
        }

        deviceRepository.saveAll(toSave);
        eventBus.publish(collected);
        eventBus.publish(new com.powersense.inventory.devices.domain.model.events.DevicesImported(toSave.size(), Instant.now()));
    }

    private DeviceCategory parseCategory(String raw) {
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
        if (raw == null) {
            throw new InvalidDeviceStatusException("Status must be 'active' or 'inactive'");
        }
        String v = raw.trim().toLowerCase(Locale.ROOT);
        return switch (v) {
            case "active" -> DeviceStatus.ACTIVE;
            case "inactive" -> DeviceStatus.INACTIVE;
            default -> throw new InvalidDeviceStatusException("Invalid status: " + raw);
        };
    }
}
