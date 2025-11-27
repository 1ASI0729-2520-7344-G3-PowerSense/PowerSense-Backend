package com.powersense.inventory.scheduling.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateScheduleResource {
	@NotBlank(message = "Device ID cannot be empty")
	private String deviceId;
	
	@NotBlank(message = "Device name cannot be empty")
	private String deviceName;
	
	@NotBlank(message = "Room name cannot be empty")
	private String roomName;
	
	private boolean enabled = true;
	
	@NotEmpty(message = "At least one schedule entry is required")
	private List<ScheduleEntryData> schedules;

	public static class ScheduleEntryData {
		@NotBlank(message = "Action cannot be empty")
		private String action; // "ON" or "OFF"
		
		@NotNull(message = "Time cannot be null")
		private TimeSlotData time;
		
		@NotEmpty(message = "At least one day must be specified")
		private List<String> days; // ["MON", "TUE", etc.]

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public TimeSlotData getTime() {
			return time;
		}

		public void setTime(TimeSlotData time) {
			this.time = time;
		}

		public List<String> getDays() {
			return days;
		}

		public void setDays(List<String> days) {
			this.days = days;
		}
	}

	public static class TimeSlotData {
		@NotNull(message = "Hour cannot be null")
		private Integer hour;
		
		@NotNull(message = "Minute cannot be null")
		private Integer minute;

		public Integer getHour() {
			return hour;
		}

		public void setHour(Integer hour) {
			this.hour = hour;
		}

		public Integer getMinute() {
			return minute;
		}

		public void setMinute(Integer minute) {
			this.minute = minute;
		}
	}

	// Getters and setters
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

	public List<ScheduleEntryData> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleEntryData> schedules) {
		this.schedules = schedules;
	}
}
