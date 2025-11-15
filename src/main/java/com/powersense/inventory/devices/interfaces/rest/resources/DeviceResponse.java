package com.powersense.inventory.devices.interfaces.rest.resources;

public class DeviceResponse {
	private String id;
	private String name;
	private String category;
	private String status;
	private LocationResponse location;
	private PowerResponse power;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocationResponse getLocation() {
		return location;
	}

	public void setLocation(LocationResponse location) {
		this.location = location;
	}

	public PowerResponse getPower() {
		return power;
	}

	public void setPower(PowerResponse power) {
		this.power = power;
	}

	public static class LocationResponse {
		private String roomId;
		private String roomName;

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

	public static class PowerResponse {
		private int watts;
		private Integer voltage;
		private Integer amperage;

		public int getWatts() {
			return watts;
		}

		public void setWatts(int watts) {
			this.watts = watts;
		}

		public Integer getVoltage() {
			return voltage;
		}

		public void setVoltage(Integer voltage) {
			this.voltage = voltage;
		}

		public Integer getAmperage() {
			return amperage;
		}

		public void setAmperage(Integer amperage) {
			this.amperage = amperage;
		}
	}
}
