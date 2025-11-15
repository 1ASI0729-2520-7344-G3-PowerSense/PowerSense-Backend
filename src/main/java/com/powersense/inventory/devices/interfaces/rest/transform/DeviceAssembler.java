package com.powersense.inventory.devices.interfaces.rest.transform;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.commands.CreateDevice;
import com.powersense.inventory.devices.domain.model.commands.UpdateDevice;
import com.powersense.inventory.devices.domain.model.commands.SetDeviceStatus;
import com.powersense.inventory.devices.interfaces.rest.resources.CreateDeviceResource;
import com.powersense.inventory.devices.interfaces.rest.resources.DeviceResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.UpdateDeviceResource;
import com.powersense.inventory.devices.interfaces.rest.resources.SetDeviceStatusResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceAssembler {
	public DeviceResponse toResponse(Device device) {
		DeviceResponse resp = new DeviceResponse();
		resp.setId(device.getId().value());
		resp.setName(device.getName().value());
		resp.setCategory(device.getCategory().value());
		resp.setStatus(device.getStatus().value());

		DeviceResponse.LocationResponse loc = new DeviceResponse.LocationResponse();
		loc.setRoomId(device.getLocation().roomId().value());
		loc.setRoomName(device.getLocation().roomName().value());
		resp.setLocation(loc);

		DeviceResponse.PowerResponse power = new DeviceResponse.PowerResponse();
		power.setWatts(device.getPower().watts());
		power.setVoltage(device.getPower().voltage());
		power.setAmperage(device.getPower().amperage());
		resp.setPower(power);

		return resp;
	}

	public List<DeviceResponse> toResponseList(List<Device> devices) {
		return devices.stream().map(this::toResponse).collect(Collectors.toList());
	}

	public CreateDevice toCreateCommand(CreateDeviceResource resource) {
		return new CreateDevice(
				resource.getName(),
				resource.getCategory(),
				resource.getRoomId(),
				resource.getRoomName(),
				resource.getWatts()
		);
	}

	public UpdateDevice toUpdateCommand(String deviceId, UpdateDeviceResource resource) {
		return new UpdateDevice(
				deviceId,
				resource.getName(),
				resource.getCategory(),
				resource.getRoomId(),
				resource.getRoomName(),
				resource.getWatts()
		);
	}

	public SetDeviceStatus toSetStatusCommand(String deviceId, SetDeviceStatusResource resource) {
		return new SetDeviceStatus(deviceId, resource.getStatus());
	}
}
