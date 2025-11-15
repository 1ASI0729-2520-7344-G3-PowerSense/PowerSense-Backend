package com.powersense.inventory.scheduling.domain.model.entities;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ActionType;
import com.powersense.inventory.scheduling.domain.services.ScheduleExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RuleAction {
	private final ActionType type;
	private final List<String> deviceIds;
	private final List<String> roomIds;
	private final List<String> excludeDeviceIds;
	private final List<String> excludeRoomIds;
	private final String category;
	private final Object value;

	public RuleAction(ActionType type, List<String> deviceIds, List<String> roomIds,
					  List<String> excludeDeviceIds, List<String> excludeRoomIds,
					  String category, Object value) {
		this.type = Objects.requireNonNull(type, "type");
		this.deviceIds = deviceIds != null ? List.copyOf(deviceIds) : List.of();
		this.roomIds = roomIds != null ? List.copyOf(roomIds) : List.of();
		this.excludeDeviceIds = excludeDeviceIds != null ? List.copyOf(excludeDeviceIds) : List.of();
		this.excludeRoomIds = excludeRoomIds != null ? List.copyOf(excludeRoomIds) : List.of();
		this.category = category;
		this.value = value;
	}

	public void execute(ScheduleExecutionContext context) {
		// No side effects in domain; execution happens via executor service in application layer.
	}

	public List<Device> getAffectedDevices(List<Device> allDevices) {
		List<Device> res = new ArrayList<>(allDevices);
		if (!deviceIds.isEmpty()) {
			res = res.stream().filter(d -> deviceIds.contains(d.getId().value())).collect(Collectors.toList());
		}
		if (!roomIds.isEmpty()) {
			res = res.stream().filter(d -> roomIds.contains(d.getLocation().roomId().value())).collect(Collectors.toList());
		}
		if (category != null && !category.isBlank()) {
			res = res.stream().filter(d -> d.getCategory().value().equalsIgnoreCase(category)).collect(Collectors.toList());
		}
		if (!excludeDeviceIds.isEmpty()) {
			res = res.stream().filter(d -> !excludeDeviceIds.contains(d.getId().value())).collect(Collectors.toList());
		}
		if (!excludeRoomIds.isEmpty()) {
			res = res.stream().filter(d -> !excludeRoomIds.contains(d.getLocation().roomId().value())).collect(Collectors.toList());
		}
		return res;
	}

	public String getDescription() {
		return type + " devices=" + deviceIds + " rooms=" + roomIds + " category=" + category;
	}

	public ActionType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public List<String> getRoomIds() {
		return roomIds;
	}

	public List<String> getExcludeDeviceIds() {
		return excludeDeviceIds;
	}

	public List<String> getExcludeRoomIds() {
		return excludeRoomIds;
	}

	public String getCategory() {
		return category;
	}
}
