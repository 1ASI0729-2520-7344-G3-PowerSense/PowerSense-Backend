package com.powersense.inventory.devices.application.internal.outboundservices.repositories;

import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findById(RoomId id);
    List<Room> findAll();
    Room save(Room room);
    void delete(RoomId id);
}




