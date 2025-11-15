package com.powersense.inventory.devices.infrastructure.persistence.inmemory;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final Map<String, Room> rooms = new HashMap<>();

    @Override
    public Optional<Room> findById(RoomId id) {
        return Optional.ofNullable(rooms.get(id.value()));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    // 🚀 (opcional) puedes añadir métodos temporales para probar:
    public void addRoom(Room room) {
        rooms.put(room.getId().value(), room);
    }
}
