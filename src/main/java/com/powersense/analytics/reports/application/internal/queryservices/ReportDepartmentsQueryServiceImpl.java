package com.powersense.analytics.reports.application.internal.queryservices;

import com.powersense.analytics.reports.application.model.DepartmentMetricResponse;
import com.powersense.analytics.reports.infrastructure.calculators.ConsumptionCalculator;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Room;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportDepartmentsQueryServiceImpl {

	private final DeviceRepository deviceRepository;
	private final RoomRepository roomRepository;
	private final ConsumptionCalculator consumptionCalculator;

	public ReportDepartmentsQueryServiceImpl(DeviceRepository deviceRepository,
											 RoomRepository roomRepository,
											 ConsumptionCalculator consumptionCalculator) {
		this.deviceRepository = deviceRepository;
		this.roomRepository = roomRepository;
		this.consumptionCalculator = consumptionCalculator;
	}

	public List<DepartmentMetricResponse> getDepartmentMetrics() {
		var devices = deviceRepository.findAll();
		Map<String, Double> consumptionByRoom = consumptionCalculator.calculateConsumptionByRoom(devices);

		return consumptionByRoom.entrySet().stream()
				.map(entry -> {
					String roomId = entry.getKey();
					String roomName = roomRepository.findById(new RoomId(roomId))
							.map(Room::getName).map(n -> n.value()).orElse(roomId);
					return new DepartmentMetricResponse(roomId, roomName, "consumption", entry.getValue());
				})
				.sorted(Comparator.comparing(DepartmentMetricResponse::getValue).reversed())
				.collect(Collectors.toList());
	}
}
