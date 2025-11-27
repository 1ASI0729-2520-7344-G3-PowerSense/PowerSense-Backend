package com.powersense.inventory.devices.interfaces.rest;

import com.powersense.inventory.devices.application.internal.commandservices.DeviceCommandServiceImpl;
import com.powersense.inventory.devices.application.internal.queryservices.RoomQueryServiceImpl;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.commands.SetRoomDevicesStatus;
import com.powersense.inventory.devices.domain.model.queries.ListRooms;
import com.powersense.inventory.devices.interfaces.rest.resources.CreateRoomResource;
import com.powersense.inventory.devices.interfaces.rest.resources.DeviceResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomDetailResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.RoomResponse;
import com.powersense.inventory.devices.interfaces.rest.resources.SetRoomDevicesStatusResource;
import com.powersense.inventory.devices.interfaces.rest.resources.UpdateRoomResource;
import com.powersense.inventory.devices.interfaces.rest.transform.DeviceAssembler;
import com.powersense.inventory.devices.interfaces.rest.transform.RoomAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/rooms")
@CrossOrigin
public class RoomController {

	private final RoomQueryServiceImpl roomQueryService;
	private final DeviceCommandServiceImpl deviceCommandService;
	private final com.powersense.inventory.devices.application.internal.commandservices.RoomCommandServiceImpl roomCommandService;
	private final RoomAssembler roomAssembler;
	private final DeviceAssembler deviceAssembler;

	public RoomController(RoomQueryServiceImpl roomQueryService,
			DeviceCommandServiceImpl deviceCommandService,
			com.powersense.inventory.devices.application.internal.commandservices.RoomCommandServiceImpl roomCommandService,
			RoomAssembler roomAssembler,
			DeviceAssembler deviceAssembler) {
		this.roomQueryService = roomQueryService;
		this.deviceCommandService = deviceCommandService;
		this.roomCommandService = roomCommandService;
		this.roomAssembler = roomAssembler;
		this.deviceAssembler = deviceAssembler;
	}

	@GetMapping
	public ResponseEntity<List<RoomResponse>> listRooms() {
		List<Room> rooms = roomQueryService.listRooms(new ListRooms());
		return ResponseEntity.ok(roomAssembler.toResponseList(rooms));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoomDetailResponse> getRoomById(@PathVariable String id) {
		try {
			Room room = roomQueryService.getRoomById(id);
			List<com.powersense.inventory.devices.domain.model.aggregates.Device> devices = roomQueryService
					.getDevicesByRoomId(id);
			return ResponseEntity.ok(roomAssembler.toDetailResponse(room, devices));
		} catch (com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}/devices")
	public ResponseEntity<List<DeviceResponse>> getRoomDevices(
			@PathVariable String id,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String category) {
		try {

			roomQueryService.getRoomById(id);

			List<com.powersense.inventory.devices.domain.model.aggregates.Device> devices = roomQueryService
					.getDevicesByRoomId(id);

			if (status != null && !status.isEmpty()) {
				String finalStatus = status.toUpperCase();
				devices = devices.stream()
						.filter(d -> d.getStatus().value().equals(finalStatus))
						.collect(java.util.stream.Collectors.toList());
			}

			if (category != null && !category.isEmpty()) {
				String finalCategory = category.toUpperCase();
				devices = devices.stream()
						.filter(d -> d.getCategory().value().equals(finalCategory))
						.collect(java.util.stream.Collectors.toList());
			}

			return ResponseEntity.ok(deviceAssembler.toResponseList(devices));
		} catch (com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomResource resource) {
		Room created = roomCommandService.createRoom(roomAssembler.toCreateCommand(resource));
		return ResponseEntity.status(HttpStatus.CREATED).body(roomAssembler.toResponse(created));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
		roomCommandService.deleteRoom(new com.powersense.inventory.devices.domain.model.commands.DeleteRoom(id));
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<RoomResponse> updateRoom(
			@PathVariable String id,
			@Valid @RequestBody UpdateRoomResource resource) {
		Room updated = roomCommandService.updateRoom(roomAssembler.toUpdateCommand(id, resource));
		return ResponseEntity.ok(roomAssembler.toResponse(updated));
	}

	@PatchMapping("/{roomId}/devices/status")
	public ResponseEntity<Void> setRoomDevicesStatus(
			@PathVariable String roomId,
			@Valid @RequestBody SetRoomDevicesStatusResource resource) {
		deviceCommandService.setRoomDevicesStatus(new SetRoomDevicesStatus(roomId, resource.getStatus()));
		return ResponseEntity.noContent().build();
	}
}
