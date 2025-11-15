package com.powersense.inventory.devices.interfaces.rest.resources;

public class UpdateDeviceResource {
	private String name;
	private String category;
	private String roomId;
	private String roomName;
	private Integer watts;

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

	public Integer getWatts() {
		return watts;
	}

	public void setWatts(Integer watts) {
		this.watts = watts;
	}
}
