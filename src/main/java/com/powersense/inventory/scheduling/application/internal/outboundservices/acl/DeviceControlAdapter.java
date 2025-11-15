package com.powersense.inventory.scheduling.application.internal.outboundservices.acl;

import com.powersense.inventory.devices.application.internal.commandservices.DeviceCommandServiceImpl;
import com.powersense.inventory.devices.application.internal.queryservices.DeviceQueryServiceImpl;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.commands.SetDeviceStatus;
import com.powersense.inventory.devices.domain.model.queries.ExportDevices;
import com.powersense.inventory.devices.domain.model.queries.ListDevices;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceControlAdapter {

	private final DeviceCommandServiceImpl deviceCommandService;
	private final DeviceQueryServiceImpl deviceQueryService;

	public DeviceControlAdapter(DeviceCommandServiceImpl deviceCommandService,
								DeviceQueryServiceImpl deviceQueryService) {
		this.deviceCommandService = deviceCommandService;
		this.deviceQueryService = deviceQueryService;
	}

	public void turnOnDevice(String deviceId) {
		deviceCommandService.setDeviceStatus(new SetDeviceStatus(deviceId, "active"));
	}

	public void turnOffDevice(String deviceId) {
		deviceCommandService.setDeviceStatus(new SetDeviceStatus(deviceId, "inactive"));
	}

	public void setDeviceStatus(String deviceId, String status) {
		deviceCommandService.setDeviceStatus(new SetDeviceStatus(deviceId, status));
	}

	public List<Device> getDevicesByRoomIds(List<String> roomIds) {
		if (roomIds == null || roomIds.isEmpty()) return List.of();
		return deviceQueryService.listDevices(new ListDevices(null, null, null, null)).stream()
				.filter(d -> roomIds.contains(d.getLocation().roomId().value()))
				.collect(Collectors.toList());
	}

	public List<Device> getDevicesByCategory(String category) {
		return deviceQueryService.listDevices(new ListDevices(null, category, null, null));
	}

	public List<Device> getAllDevices() {
		return deviceQueryService.exportDevices(new ExportDevices());
	}
}
