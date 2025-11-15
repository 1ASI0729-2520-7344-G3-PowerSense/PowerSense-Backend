package com.powersense.inventory.devices.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SetAllDevicesStatusResource {
	@NotBlank
	@Pattern(regexp = "active|inactive")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
