package com.powersense.inventory.devices.interfaces.rest.transform;

import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.interfaces.rest.resources.CreateRoomResource;
import com.powersense.inventory.devices.interfaces.rest.resources.DeviceResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomDetailResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.UpdateRoomResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomAssembler {
	public RoomResponse toResponse(Room room) {
		RoomResponse resp = new RoomResponse();
		resp.setId(room.getId().value());
		resp.setName(room.getName().value());
		return resp;
	}

	public List<RoomResponse> toResponseList(List<Room> rooms) {
		return rooms.stream().map(this::toResponse).collect(Collectors.toList());
	}

	public com.powersense.inventory.devices.domain.model.commands.CreateRoom toCreateCommand(
			CreateRoomResource resource) {
		return new com.powersense.inventory.devices.domain.model.commands.CreateRoom(resource.getName());
	}

	public com.powersense.inventory.devices.domain.model.commands.UpdateRoom toUpdateCommand(String id,
			UpdateRoomResource resource) {
		return new com.powersense.inventory.devices.domain.model.commands.UpdateRoom(id, resource.getName());
	}

	public RoomDetailResponse toDetailResponse(Room room,
			List<com.powersense.inventory.devices.domain.model.aggregates.Device> devices) {
		RoomDetailResponse response = new RoomDetailResponse();
		response.setId(room.getId().value());
		response.setName(room.getName().value());
		response.setDeviceCount(devices.size());

		List<DeviceResponse> deviceResponses = devices.stream()
				.map(this::deviceToResponse)
				.collect(Collectors.toList());
		response.setDevices(deviceResponses);

		return response;
	}

	private DeviceResponse deviceToResponse(com.powersense.inventory.devices.domain.model.aggregates.Device device) {
		DeviceResponse resp = new DeviceResponse();
		resp.setId(device.getId().value());
		resp.setName(device.getName().value());
		resp.setCategory(device.getCategory().name());
		resp.setStatus(device.getStatus().value());

		DeviceResponse.LocationResponse location = new DeviceResponse.LocationResponse();
		location.setRoomId(device.getLocation().roomId().value());
		location.setRoomName(device.getLocation().roomName().value());
		resp.setLocation(location);

		DeviceResponse.PowerResponse power = new DeviceResponse.PowerResponse();
		power.setWatts(device.getPower().watts());
		resp.setPower(power);

		return resp;
	}
}
