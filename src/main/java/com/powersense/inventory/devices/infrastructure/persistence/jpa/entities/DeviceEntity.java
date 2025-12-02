package com.powersense.inventory.devices.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceCategory;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceName;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.devices.domain.model.valueobjects.Location;
import com.powersense.inventory.devices.domain.model.valueobjects.PowerSpecification;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "devices")
public class DeviceEntity {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceCategory category;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceStatus status;

	@Embedded
	private LocationEmbeddable location;

	@Embedded
	private PowerSpecificationEmbeddable power;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "created_at")
	private Instant createdAt;

	@Column(name = "updated_at")
	private Instant updatedAt;

	public DeviceEntity() {
	}

	public DeviceEntity(String id, String name, DeviceCategory category, DeviceStatus status,
			LocationEmbeddable location, PowerSpecificationEmbeddable power,
			Long userId, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.status = status;
		this.location = location;
		this.power = power;
		this.userId = userId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static DeviceEntity fromDomain(Device device) {
		Instant now = Instant.now();
		return new DeviceEntity(
				device.getId().value(),
				device.getName().value(),
				device.getCategory(),
				device.getStatus(),
				LocationEmbeddable.fromDomain(device.getLocation()),
				PowerSpecificationEmbeddable.fromDomain(device.getPower()),
				device.getUserId(),
				now,
				now);
	}

	public Device toDomain() {
		return new Device(
				new DeviceId(id),
				new DeviceName(name),
				category,
				status,
				location.toDomain(),
				power.toDomain(),
				userId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceCategory getCategory() {
		return category;
	}

	public void setCategory(DeviceCategory category) {
		this.category = category;
	}

	public DeviceStatus getStatus() {
		return status;
	}

	public void setStatus(DeviceStatus status) {
		this.status = status;
	}

	public LocationEmbeddable getLocation() {
		return location;
	}

	public void setLocation(LocationEmbeddable location) {
		this.location = location;
	}

	public PowerSpecificationEmbeddable getPower() {
		return power;
	}

	public void setPower(PowerSpecificationEmbeddable power) {
		this.power = power;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
