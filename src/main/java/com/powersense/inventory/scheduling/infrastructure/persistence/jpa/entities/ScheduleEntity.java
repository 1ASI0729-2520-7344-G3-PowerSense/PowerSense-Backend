package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleEntity {
	@Id
	private String id;

	@Column(name = "device_id", nullable = false, unique = true)
	private String deviceId;

	@Column(name = "device_name", nullable = false)
	private String deviceName;

	@Column(name = "room_name", nullable = false)
	private String roomName;

	@Column(nullable = false)
	private boolean enabled;

	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScheduleEntryEntity> entries = new ArrayList<>();

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public static ScheduleEntity fromDomain(Schedule schedule) {
		ScheduleEntity e = new ScheduleEntity();
		e.id = schedule.getId().value();
		e.deviceId = schedule.getDeviceId().value();
		e.deviceName = schedule.getDeviceName();
		e.roomName = schedule.getRoomName();
		e.enabled = schedule.isEnabled();
		e.userId = schedule.getUserId();
		e.createdAt = schedule.getCreatedAt();
		e.updatedAt = schedule.getUpdatedAt();
		e.entries = schedule.getEntries().stream()
				.map(ScheduleEntryEntity::fromDomain)
				.collect(Collectors.toList());
		e.entries.forEach(en -> en.setSchedule(e));
		return e;
	}

	public Schedule toDomain() {
		List<ScheduleEntry> domainEntries = entries.stream().map(ScheduleEntryEntity::toDomain).toList();
		return new Schedule(
				new ScheduleId(id),
				new DeviceId(deviceId),
				deviceName,
				roomName,
				enabled,
				domainEntries,
				userId);
	}

	public void addEntry(ScheduleEntryEntity entry) {
		entry.setSchedule(this);
		this.entries.add(entry);
	}

	public void removeEntry(ScheduleEntryEntity entry) {
		entry.setSchedule(null);
		this.entries.remove(entry);
	}
}
