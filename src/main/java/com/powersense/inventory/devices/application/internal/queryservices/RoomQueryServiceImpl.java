package com.powersense.inventory.devices.application.internal.queryservices;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
import com.powersense.inventory.devices.domain.exceptions.RoomNotFoundException;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.queries.ListRooms;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomQueryServiceImpl {

    private final RoomRepository roomRepository;

    public RoomQueryServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> listRooms(ListRooms query) {
        return roomRepository.findAll();
    }

    public Room getRoomById(String roomId) {
        return roomRepository.findById(new RoomId(roomId))
                .orElseThrow(() -> new RoomNotFoundException(roomId));
    }
}
