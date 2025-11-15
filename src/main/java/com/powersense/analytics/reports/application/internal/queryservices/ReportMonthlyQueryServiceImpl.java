package com.powersense.analytics.reports.application.internal.queryservices;

import com.powersense.analytics.reports.application.model.MonthlyComparisonResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ReportMonthlyQueryServiceImpl {

	public List<MonthlyComparisonResponse> getMonthlyComparison() {
		return Arrays.asList(
				new MonthlyComparisonResponse("Ene", 3250, 3000),
				new MonthlyComparisonResponse("Feb", 3100, 2850),
				new MonthlyComparisonResponse("Mar", 3300, 3050),
				new MonthlyComparisonResponse("Abr", 3150, 2900),
				new MonthlyComparisonResponse("May", 3200, 2950),
				new MonthlyComparisonResponse("Jun", 3100, 2850),
				new MonthlyComparisonResponse("Jul", 3250, 3000),
				new MonthlyComparisonResponse("Ago", 3300, 3100),
				new MonthlyComparisonResponse("Sep", 3150, 2950),
				new MonthlyComparisonResponse("Oct", 3200, 3000),
				new MonthlyComparisonResponse("Nov", 3250, 3050),
				new MonthlyComparisonResponse("Dic", 3200, 3050)
		);
	}
}
