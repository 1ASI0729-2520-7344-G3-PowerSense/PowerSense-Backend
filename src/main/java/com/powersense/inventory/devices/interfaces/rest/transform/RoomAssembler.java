package com.powersense.inventory.devices.interfaces.rest.transform;

import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomResponse;
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
}
