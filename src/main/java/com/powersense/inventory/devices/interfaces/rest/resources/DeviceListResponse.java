package com.powersense.inventory.devices.interfaces.rest.resources;

import java.util.List;

public class DeviceListResponse {
	private List<DeviceResponse> devices;
	private int total;

	public DeviceListResponse() {
	}

	public DeviceListResponse(List<DeviceResponse> devices, int total) {
		this.devices = devices;
		this.total = total;
	}

	public List<DeviceResponse> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceResponse> devices) {
		this.devices = devices;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
