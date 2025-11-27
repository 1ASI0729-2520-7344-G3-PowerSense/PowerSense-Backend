package com.powersense.inventory.devices.application.internal.queryservices;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
import com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.queries.GetRoomById;
import com.powersense.inventory.devices.domain.model.queries.ListRooms;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomQueryServiceImpl {

    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;

    public RoomQueryServiceImpl(RoomRepository roomRepository, DeviceRepository deviceRepository) {
        this.roomRepository = roomRepository;
        this.deviceRepository = deviceRepository;
    }

    public List<Room> listRooms(ListRooms query) {
        return roomRepository.findAll();
    }

    public Room getRoomById(String roomId) {
        return roomRepository.findById(new RoomId(roomId))
                .orElseThrow(() -> new RoomNotFoundException(roomId));
    }

    public Room getRoomByIdWithDevices(GetRoomById query) {
        return roomRepository.findById(new RoomId(query.roomId()))
                .orElseThrow(() -> new RoomNotFoundException(query.roomId()));
    }

    public List<Device> getDevicesByRoomId(String roomId) {
        return deviceRepository.findByRoomId(new RoomId(roomId));
    }
}
