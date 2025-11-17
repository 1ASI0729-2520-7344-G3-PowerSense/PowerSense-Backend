package com.powersense.inventory.devices.interfaces.rest;

import com.powersense.inventory.devices.application.internal.commandservices.DeviceCommandServiceImpl;
import com.powersense.inventory.devices.application.internal.queryservices.DeviceQueryServiceImpl;
import com.powersense.inventory.devices.domain.exceptions.DeviceNotFoundException;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.commands.DeleteDevice;
import com.powersense.inventory.devices.domain.model.commands.ImportDevices;
import com.powersense.inventory.devices.domain.model.commands.SetAllDevicesStatus;
import com.powersense.inventory.devices.domain.model.commands.SetDeviceStatus;
import com.powersense.inventory.devices.domain.model.queries.ExportDevices;
import com.powersense.inventory.devices.domain.model.queries.GetDeviceById;
import com.powersense.inventory.devices.domain.model.queries.ListDevices;
import com.powersense.inventory.devices.interfaces.rest.resources.*;
import com.powersense.inventory.devices.interfaces.rest.transform.DeviceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/devices")
@CrossOrigin
public class DeviceController {

	private final DeviceCommandServiceImpl deviceCommandService;
	private final DeviceQueryServiceImpl deviceQueryService;
	private final DeviceAssembler deviceAssembler;

	public DeviceController(DeviceCommandServiceImpl deviceCommandService,
							DeviceQueryServiceImpl deviceQueryService,
							DeviceAssembler deviceAssembler) {
		this.deviceCommandService = deviceCommandService;
		this.deviceQueryService = deviceQueryService;
		this.deviceAssembler = deviceAssembler;
	}

	@GetMapping
	public ResponseEntity<List<DeviceResponse>> listDevices(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String roomId,
			@RequestParam(required = false) String status
	) {
		ListDevices query = new ListDevices(search, category, roomId, status);
		List<Device> devices = deviceQueryService.listDevices(query);
		return ResponseEntity.ok(deviceAssembler.toResponseList(devices));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable String id) {
		try {
			Device device = deviceQueryService.getDeviceById(new GetDeviceById(id));
			return ResponseEntity.ok(deviceAssembler.toResponse(device));
		} catch (DeviceNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody CreateDeviceResource resource) {
		Device created = deviceCommandService.createDevice(deviceAssembler.toCreateCommand(resource));
		return ResponseEntity.status(HttpStatus.CREATED).body(deviceAssembler.toResponse(created));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<DeviceResponse> updateDevice(
			@PathVariable String id,
			@Valid @RequestBody UpdateDeviceResource resource
	) {
		Device updated = deviceCommandService.updateDevice(deviceAssembler.toUpdateCommand(id, resource));
		return ResponseEntity.ok(deviceAssembler.toResponse(updated));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
		deviceCommandService.deleteDevice(new DeleteDevice(id));
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<DeviceResponse> setDeviceStatus(
			@PathVariable String id,
			@Valid @RequestBody SetDeviceStatusResource resource
	) {
		Device updated = deviceCommandService.setDeviceStatus(new SetDeviceStatus(id, resource.getStatus()));
		return ResponseEntity.ok(deviceAssembler.toResponse(updated));
	}

	@PatchMapping("/status/all")
	public ResponseEntity<Void> setAllDevicesStatus(@Valid @RequestBody SetAllDevicesStatusResource resource) {
		deviceCommandService.setAllDevicesStatus(new SetAllDevicesStatus(resource.getStatus()));
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importDevices(@Valid @RequestBody ImportDevicesResource resource) {
		List<ImportDevices.DeviceData> data = resource.getDevices().stream()
				.map(d -> new ImportDevices.DeviceData(d.getName(), d.getCategory(), d.getRoomId(), d.getRoomName(), d.getWatts()))
				.toList();
		deviceCommandService.importDevices(new ImportDevices(data));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/export")
	public ResponseEntity<List<DeviceResponse>> exportDevices() {
		List<Device> devices = deviceQueryService.exportDevices(new ExportDevices());
		return ResponseEntity.ok(deviceAssembler.toResponseList(devices));
	}
}

