package com.powersense.inventory.devices.application.internal.queryservices;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.exceptions.DeviceNotFoundException;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.queries.ExportDevices;
import com.powersense.inventory.devices.domain.model.queries.GetDeviceById;
import com.powersense.inventory.devices.domain.model.queries.ListDevices;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.auth.infrastructure.tokens.JwtTokenService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DeviceQueryServiceImpl {

    private final DeviceRepository deviceRepository;
    private final JwtTokenService jwtTokenService;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository, JwtTokenService jwtTokenService) {
        this.deviceRepository = deviceRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public List<Device> listDevices(ListDevices query) {
        Long userId = getCurrentUserId();
        List<Device> all = userId != null ? deviceRepository.findAllByUserId(userId) : deviceRepository.findAll();
        if (query == null)
            return all;

        String search = normalize(query.search());
        String category = normalize(query.category());
        String roomId = normalize(query.roomId());
        String status = normalize(query.status());

        return all.stream()
                .filter(d -> filterSearch(d, search))
                .filter(d -> filterCategory(d, category))
                .filter(d -> filterRoomId(d, roomId))
                .filter(d -> filterStatus(d, status))
                .collect(Collectors.toList());
    }

    public Device getDeviceById(GetDeviceById query) {
        DeviceId id = new DeviceId(query.deviceId());
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id.value()));
    }

    public List<Device> exportDevices(ExportDevices query) {
        Long userId = getCurrentUserId();
        return userId != null ? deviceRepository.findAllByUserId(userId) : deviceRepository.findAll();
    }

    private boolean filterSearch(Device d, String search) {
        if (search == null)
            return true;
        String name = d.getName().value().toLowerCase(Locale.ROOT);
        String roomName = d.getLocation().roomName().value().toLowerCase(Locale.ROOT);
        return name.contains(search) || roomName.contains(search);
    }

    private boolean filterCategory(Device d, String category) {
        if (category == null)
            return true;
        return d.getCategory().value().equalsIgnoreCase(category) ||
                d.getCategory().name().equalsIgnoreCase(category);
    }

    private boolean filterRoomId(Device d, String roomId) {
        if (roomId == null)
            return true;
        return d.getLocation().roomId().value().equalsIgnoreCase(roomId);
    }

    private boolean filterStatus(Device d, String status) {
        if (status == null)
            return true;
        return d.getStatus().value().equalsIgnoreCase(status) ||
                d.getStatus().name().equalsIgnoreCase(status);
    }

    private String normalize(String s) {
        if (s == null)
            return null;
        String t = s.trim();
        return t.isEmpty() ? null : t.toLowerCase(Locale.ROOT);
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
