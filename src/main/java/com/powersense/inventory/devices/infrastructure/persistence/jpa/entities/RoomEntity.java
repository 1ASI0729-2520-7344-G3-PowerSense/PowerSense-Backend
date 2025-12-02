package com.powersense.inventory.devices.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class RoomEntity {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(name = "user_id")
	private Long userId;

	public RoomEntity() {
	}

	public RoomEntity(String id, String name, Long userId) {
		this.id = id;
		this.name = name;
		this.userId = userId;
	}

	public static RoomEntity fromDomain(Room room) {
		return new RoomEntity(room.getId().value(), room.getName().value(), room.getUserId());
	}

	public Room toDomain() {
		return new Room(new RoomId(id), new RoomName(name), userId);
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
