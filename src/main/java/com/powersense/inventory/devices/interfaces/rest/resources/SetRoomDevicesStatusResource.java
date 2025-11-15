package com.powersense.inventory.devices.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SetRoomDevicesStatusResource {
	@NotBlank
	private String roomId;

	@NotBlank
	@Pattern(regexp = "active|inactive")
	private String status;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
