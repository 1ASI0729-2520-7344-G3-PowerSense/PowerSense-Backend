package com.powersense.inventory.devices.interfaces.rest.resources;

import java.util.List;

public class RoomDetailResponse {
	private String id;
	private String name;
	private int deviceCount;
	private List<DeviceResponse> devices;

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

	public int getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}

	public List<DeviceResponse> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceResponse> devices) {
		this.devices = devices;
	}
}
