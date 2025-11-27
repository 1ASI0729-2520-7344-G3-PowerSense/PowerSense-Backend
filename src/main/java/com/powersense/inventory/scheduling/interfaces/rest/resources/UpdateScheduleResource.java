package com.powersense.inventory.scheduling.interfaces.rest.resources;

import java.util.List;

public class UpdateScheduleResource {
	private Boolean enabled;
	private List<ScheduleEntryData> schedules;

	public static class ScheduleEntryData {
		private String action;
		private TimeSlotData time;
		private List<String> days;

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
		private Integer hour;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<ScheduleEntryData> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleEntryData> schedules) {
		this.schedules = schedules;
	}
}
