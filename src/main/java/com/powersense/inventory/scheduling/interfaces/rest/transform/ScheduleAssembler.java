package com.powersense.inventory.scheduling.interfaces.rest.transform;

import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.commands.CreateSchedule;
import com.powersense.inventory.scheduling.domain.model.commands.UpdateSchedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.interfaces.rest.resources.CreateScheduleResource;
import com.powersense.inventory.scheduling.interfaces.rest.resources.ScheduleResponse;
import com.powersense.inventory.scheduling.interfaces.rest.resources.UpdateScheduleResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleAssembler {

	public ScheduleResponse toResponse(Schedule schedule) {
		ScheduleResponse response = new ScheduleResponse();
		response.setId(schedule.getId().value());
		response.setDeviceId(schedule.getDeviceId().value());
		response.setDeviceName(schedule.getDeviceName());
		response.setRoomName(schedule.getRoomName());
		response.setEnabled(schedule.isEnabled());
		response.setCreatedAt(schedule.getCreatedAt());
		response.setUpdatedAt(schedule.getUpdatedAt());

		List<ScheduleResponse.ScheduleEntryResponse> entries = schedule.getEntries().stream()
				.map(this::toEntryResponse)
				.collect(Collectors.toList());
		response.setSchedules(entries);

		return response;
	}

	public List<ScheduleResponse> toResponseList(List<Schedule> schedules) {
		return schedules.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	public CreateSchedule toCreateCommand(CreateScheduleResource resource) {
		List<CreateSchedule.ScheduleEntryData> entries = resource.getSchedules().stream()
				.map(e -> new CreateSchedule.ScheduleEntryData(
						e.getAction(),
						new CreateSchedule.TimeSlotData(e.getTime().getHour(), e.getTime().getMinute()),
						e.getDays()))
				.collect(Collectors.toList());

		return new CreateSchedule(
				resource.getDeviceId(),
				resource.getDeviceName(),
				resource.getRoomName(),
				resource.isEnabled(),
				entries);
	}

	public UpdateSchedule toUpdateCommand(String scheduleId, UpdateScheduleResource resource) {
		List<CreateSchedule.ScheduleEntryData> entries = null;

		if (resource.getSchedules() != null) {
			entries = resource.getSchedules().stream()
					.map(e -> new CreateSchedule.ScheduleEntryData(
							e.getAction(),
							new CreateSchedule.TimeSlotData(e.getTime().getHour(), e.getTime().getMinute()),
							e.getDays()))
					.collect(Collectors.toList());
		}

		return new UpdateSchedule(scheduleId, resource.getEnabled(), entries);
	}

	private ScheduleResponse.ScheduleEntryResponse toEntryResponse(ScheduleEntry entry) {
		ScheduleResponse.ScheduleEntryResponse response = new ScheduleResponse.ScheduleEntryResponse();
		response.setAction(entry.getAction().name());
		response.setTime(String.format("%02d:%02d",
				entry.getTime().toLocalTime().getHour(),
				entry.getTime().toLocalTime().getMinute()));
		response.setDays(entry.getDays().stream()
				.map(Enum::name)
				.collect(Collectors.toList()));
		return response;
	}
}
