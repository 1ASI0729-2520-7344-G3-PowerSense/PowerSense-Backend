package com.powersense.inventory.devices.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateRoomResource {
	@NotBlank(message = "Room name cannot be empty")
	@Size(min = 1, max = 100, message = "Room name must be between 1 and 100 characters")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
