package com.powersense.analytics.reports.application.internal.queryservices;

import com.powersense.analytics.reports.application.model.ReportHistoryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ReportHistoryQueryServiceImpl {

	public List<ReportHistoryResponse> getReportHistory() {
		return Arrays.asList(
				new ReportHistoryResponse("hist-1", LocalDate.of(2025, 1, 31),
						"Reporte Enero 2025", "consumption", "Administración", 847, 423, -5),
				new ReportHistoryResponse("hist-2", LocalDate.of(2025, 1, 31),
						"Reporte Enero 2025", "consumption", "Producción", 1456, 728, 12),
				new ReportHistoryResponse("hist-3", LocalDate.of(2024, 12, 31),
						"Reporte Diciembre 2024", "efficiency", "General", 0, 0, -3),
				new ReportHistoryResponse("hist-4", LocalDate.of(2024, 12, 31),
						"Reporte Diciembre 2024", "costs", "General", 2980, 1490, 3)
		);
	}
}
