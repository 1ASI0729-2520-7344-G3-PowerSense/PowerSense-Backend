package com.powersense.inventory.devices.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.devices.domain.model.valueobjects.Location;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LocationEmbeddable {
	@Column(name = "room_id", nullable = false)
	private String roomId;

	@Column(name = "room_name", nullable = false)
	private String roomName;

	public LocationEmbeddable() {
	}

	public LocationEmbeddable(String roomId, String roomName) {
		this.roomId = roomId;
		this.roomName = roomName;
	}

	public static LocationEmbeddable fromDomain(Location location) {
		return new LocationEmbeddable(location.roomId().value(), location.roomName().value());
	}

	public Location toDomain() {
		return new Location(new RoomId(roomId), new RoomName(roomName));
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
}


