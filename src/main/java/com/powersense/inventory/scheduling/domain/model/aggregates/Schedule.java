package com.powersense.inventory.scheduling.domain.model.aggregates;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.events.*;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Schedule {
	private final ScheduleId id;
	private final DeviceId deviceId;
	private String deviceName;
	private String roomName;
	private boolean enabled;
	private final List<ScheduleEntry> entries = new ArrayList<>();
	private Instant createdAt;
	private Instant updatedAt;

	private final List<DomainEvent> domainEvents = new ArrayList<>();

	public Schedule(ScheduleId id, DeviceId deviceId, String deviceName, String roomName, boolean enabled,
					List<ScheduleEntry> initialEntries) {
		this.id = Objects.requireNonNull(id, "id");
		this.deviceId = Objects.requireNonNull(deviceId, "deviceId");
		this.deviceName = Objects.requireNonNull(deviceName, "deviceName");
		this.roomName = Objects.requireNonNull(roomName, "roomName");
		this.enabled = enabled;
		if (initialEntries != null) {
			this.entries.addAll(initialEntries);
		}
		this.entries.sort(Comparator.comparing(e -> e.getTime().toLocalTime()));
		this.createdAt = Instant.now();
		this.updatedAt = this.createdAt;
		registerEvent(new ScheduleCreated(id.value(), deviceId.value(), createdAt));
	}

	public void enable() {
		if (!enabled) {
			if (entries.isEmpty()) {
				throw new IllegalStateException("Cannot enable schedule without entries");
			}
			this.enabled = true;
			this.updatedAt = Instant.now();
			registerEvent(new ScheduleEnabled(id.value(), deviceId.value(), updatedAt));
		}
	}

	public void disable() {
		if (enabled) {
			this.enabled = false;
			this.updatedAt = Instant.now();
			registerEvent(new ScheduleDisabled(id.value(), deviceId.value(), updatedAt));
		}
	}

	public void addEntry(ScheduleEntry entry) {
		this.entries.add(Objects.requireNonNull(entry));
		this.entries.sort(Comparator.comparing(e -> e.getTime().toLocalTime()));
		this.updatedAt = Instant.now();
		registerEvent(new ScheduleUpdated(id.value(), updatedAt));
	}

	public void removeEntry(int index) {
		this.entries.remove(index);
		this.updatedAt = Instant.now();
		registerEvent(new ScheduleUpdated(id.value(), updatedAt));
	}

	public void updateEntry(int index, ScheduleEntry newEntry) {
		this.entries.set(index, Objects.requireNonNull(newEntry));
		this.entries.sort(Comparator.comparing(e -> e.getTime().toLocalTime()));
		this.updatedAt = Instant.now();
		registerEvent(new ScheduleUpdated(id.value(), updatedAt));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public List<ScheduleEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public void validateNoTimeConflicts() {
		// Basic rule: no two entries with same action at the same time on the same day
		for (int i = 0; i < entries.size(); i++) {
			for (int j = i + 1; j < entries.size(); j++) {
				var a = entries.get(i);
				var b = entries.get(j);
				if (a.getAction() == b.getAction() &&
						a.getTime().toLocalTime().equals(b.getTime().toLocalTime())) {
					// intersecting days?
					boolean overlap = a.getDays().stream().anyMatch(d -> b.getDays().contains(d));
					if (overlap) {
						throw new IllegalStateException("Overlapping schedule entries for same action/time");
					}
				}
			}
		}
		if (enabled && entries.isEmpty()) {
			throw new IllegalStateException("Enabled schedule must have at least one entry");
		}
	}

	public List<DomainEvent> pullDomainEvents() {
		List<DomainEvent> out = List.copyOf(domainEvents);
		domainEvents.clear();
		return out;
	}

	private void registerEvent(DomainEvent event) {
		domainEvents.add(event);
	}

	public ScheduleId getId() {
		return id;
	}

	public DeviceId getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getRoomName() {
		return roomName;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
