package com.powersense.inventory.devices.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class ImportDevicesResource {
	@NotNull
	@Valid
	private List<DeviceImportItem> devices;

	public List<DeviceImportItem> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceImportItem> devices) {
		this.devices = devices;
	}

	public static class DeviceImportItem {
		@NotBlank
		private String name;

		@NotBlank
		private String category;

		@NotBlank
		private String roomId;

		@NotBlank
		private String roomName;

		@Positive
		private int watts;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
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

		public int getWatts() {
			return watts;
		}

		public void setWatts(int watts) {
			this.watts = watts;
		}
	}
}
