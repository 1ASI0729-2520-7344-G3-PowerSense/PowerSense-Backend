package com.powersense.inventory.scheduling.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public class ScheduleResponse {
	private String id;
	private String deviceId;
	private String deviceName;
	private String roomName;
	private boolean enabled;
	private List<ScheduleEntryResponse> schedules;
	private Instant createdAt;
	private Instant updatedAt;

	public static class ScheduleEntryResponse {
		private String action;
		private String time; // "HH:mm" format
		private List<String> days;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public List<String> getDays() {
			return days;
		}

		public void setDays(List<String> days) {
			this.days = days;
		}
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<ScheduleEntryResponse> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleEntryResponse> schedules) {
		this.schedules = schedules;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
