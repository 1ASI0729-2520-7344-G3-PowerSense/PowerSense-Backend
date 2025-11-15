package com.powersense.inventory.devices.domain.model.aggregates;

import com.powersense.inventory.devices.domain.model.events.DeviceCreated;
import com.powersense.inventory.devices.domain.model.events.DeviceStatusChanged;
import com.powersense.inventory.devices.domain.model.events.DomainEvent;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceCategory;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceName;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.devices.domain.model.valueobjects.Location;
import com.powersense.inventory.devices.domain.model.valueobjects.PowerSpecification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Device {
    private final DeviceId id;
    private DeviceName name;
    private DeviceCategory category;
    private DeviceStatus status;
    private Location location;
    private PowerSpecification power;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Device(DeviceId id,
                  DeviceName name,
                  DeviceCategory category,
                  DeviceStatus status,
                  Location location,
                  PowerSpecification power) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.category = Objects.requireNonNull(category, "category");
        this.status = Objects.requireNonNull(status, "status");
        this.location = Objects.requireNonNull(location, "location");
        this.power = Objects.requireNonNull(power, "power");
    }

    public static Device create(DeviceId id,
                                DeviceName name,
                                DeviceCategory category,
                                Location location,
                                PowerSpecification power) {
        Device device = new Device(id, name, category, DeviceStatus.INACTIVE, location, power);
        device.registerEvent(new DeviceCreated(id.value(), name.value(), Instant.now()));
        return device;
    }

    public void activate() {
        if (status.isInactive()) {
            DeviceStatus previous = this.status;
            this.status = DeviceStatus.ACTIVE;
            registerEvent(new DeviceStatusChanged(id.value(), previous.value(), this.status.value(), Instant.now()));
        }
    }

    public void deactivate() {
        if (status.isActive()) {
            DeviceStatus previous = this.status;
            this.status = DeviceStatus.INACTIVE;
            registerEvent(new DeviceStatusChanged(id.value(), previous.value(), this.status.value(), Instant.now()));
        }
    }

    public void changeLocation(Location newLocation) {
        this.location = Objects.requireNonNull(newLocation, "newLocation");
    }

    public void updatePower(PowerSpecification power) {
        this.power = Objects.requireNonNull(power, "power");
    }

    public boolean isActive() {
        return status.isActive();
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public DeviceId getId() {
        return id;
    }

    public DeviceName getName() {
        return name;
    }

    public DeviceCategory getCategory() {
        return category;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public PowerSpecification getPower() {
        return power;
    }
}
