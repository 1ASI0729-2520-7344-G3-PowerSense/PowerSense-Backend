package com.powersense.inventory.devices.infrastructure.rest;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.infrastructure.persistence.inmemory.InMemoryDeviceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final InMemoryDeviceRepository deviceRepository;

    public DeviceController(InMemoryDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable String id) {
        return deviceRepository.findById(new DeviceId(id)).orElse(null);
    }
}


