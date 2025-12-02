package com.powersense.inventory.devices.application.internal.commandservices;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.commands.CreateRoom;
import com.powersense.inventory.devices.domain.model.commands.DeleteRoom;
import com.powersense.inventory.devices.domain.model.commands.UpdateRoom;
import com.powersense.inventory.devices.domain.model.valueobjects.Location;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomName;
import com.powersense.auth.infrastructure.tokens.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;

@Service
public class RoomCommandServiceImpl {

	private final RoomRepository roomRepository;
	private final DeviceRepository deviceRepository;
	private final JwtTokenService jwtTokenService;

	public RoomCommandServiceImpl(RoomRepository roomRepository, DeviceRepository deviceRepository,
			JwtTokenService jwtTokenService) {
		this.roomRepository = roomRepository;
		this.deviceRepository = deviceRepository;
		this.jwtTokenService = jwtTokenService;
	}

	public Room createRoom(CreateRoom command) {
		Long userId = getCurrentUserId();
		String generatedId = UUID.randomUUID().toString();

		Room room = Room.create(
				new RoomId(generatedId),
				new RoomName(command.name()),
				userId);

		return roomRepository.save(room);
	}

	public void deleteRoom(DeleteRoom command) {
		RoomId roomId = new RoomId(command.roomId());

		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException(
						command.roomId()));

		List<Device> devicesInRoom = deviceRepository.findByRoomId(roomId);
		for (Device device : devicesInRoom) {
			deviceRepository.deleteById(device.getId());
		}

		roomRepository.delete(roomId);
	}

	public Room updateRoom(UpdateRoom command) {
		RoomId roomId = new RoomId(command.roomId());

		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException(
						command.roomId()));

		room.updateName(new RoomName(command.name()));
		Room updatedRoom = roomRepository.save(room);

		List<Device> devicesInRoom = deviceRepository.findByRoomId(roomId);
		for (Device device : devicesInRoom) {
			Location currentLocation = device.getLocation();

			Location updatedLocation = new Location(
					currentLocation.roomId(),
					new RoomName(command.name()));
			device.changeLocation(updatedLocation);
		}

		if (!devicesInRoom.isEmpty()) {
			deviceRepository.saveAll(devicesInRoom);
		}

		return updatedRoom;
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		String token = (String) authentication.getCredentials();
		if (token == null) {
			return null;
		}
		return jwtTokenService.extractUserId(token);
	}
}
