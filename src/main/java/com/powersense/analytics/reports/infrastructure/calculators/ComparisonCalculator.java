package com.powersense.analytics.reports.infrastructure.calculators;

import com.powersense.analytics.reports.application.model.ReportKPIsResponse;
import org.springframework.stereotype.Component;

@Component
public class ComparisonCalculator {

	public int calculateVariationPercentage(double current, double previous) {
		if (previous == 0) return 0;
		return (int) Math.round(((current - previous) / previous) * 100.0);
	}

	public ReportKPIsResponse.ComparisonResponse compareWithPreviousPeriod(double currentConsumption) {
		// Mock: previous is 12% higher consumption
		double previous = currentConsumption * 1.12;
		int consumptionPct = calculateVariationPercentage(currentConsumption, previous); // ~ -12
		int costPct = -8;
		int efficiencyPct = 3;
		return new ReportKPIsResponse.ComparisonResponse(consumptionPct, costPct, efficiencyPct);
	}
}
