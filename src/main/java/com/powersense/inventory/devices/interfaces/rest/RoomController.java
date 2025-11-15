package com.powersense.inventory.devices.interfaces.rest;

import com.powersense.inventory.devices.application.internal.commandservices.DeviceCommandServiceImpl;
import com.powersense.inventory.devices.application.internal.queryservices.RoomQueryServiceImpl;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.commands.SetRoomDevicesStatus;
import com.powersense.inventory.devices.domain.model.queries.ListRooms;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.SetRoomDevicesStatusResource;
import com.powersense.inventory.devices.interfaces.rest.transform.RoomAssembler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/rooms")
@CrossOrigin
public class RoomController {

	private final RoomQueryServiceImpl roomQueryService;
	private final DeviceCommandServiceImpl deviceCommandService;
	private final RoomAssembler roomAssembler;

	public RoomController(RoomQueryServiceImpl roomQueryService,
						  DeviceCommandServiceImpl deviceCommandService,
						  RoomAssembler roomAssembler) {
		this.roomQueryService = roomQueryService;
		this.deviceCommandService = deviceCommandService;
		this.roomAssembler = roomAssembler;
	}

	@GetMapping
	public ResponseEntity<List<RoomResponse>> listRooms() {
		List<Room> rooms = roomQueryService.listRooms(new ListRooms());
		return ResponseEntity.ok(roomAssembler.toResponseList(rooms));
	}

	@PatchMapping("/{roomId}/devices/status")
	public ResponseEntity<Void> setRoomDevicesStatus(
			@PathVariable String roomId,
			@Valid @RequestBody SetRoomDevicesStatusResource resource
	) {
		deviceCommandService.setRoomDevicesStatus(new SetRoomDevicesStatus(roomId, resource.getStatus()));
		return ResponseEntity.noContent().build();
	}
}


