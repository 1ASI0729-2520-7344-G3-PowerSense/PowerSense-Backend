package com.powersense.analytics.dashboard.interfaces.rest.resources;

public class DashboardKPIsResponse {

    private int totalDevices;
    private long activeDevices;
    private int inactiveDevices;
    private double totalConsumptionKWh;
    private double totalCostUSD;
    private int efficiencyPct;
    private double estimatedMonthlyCost;
    private double monthlySavings;
    private ComparisonResponse comparison;

    public int getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(int totalDevices) {
        this.totalDevices = totalDevices;
    }

    public long getActiveDevices() {
        return activeDevices;
    }

    public void setActiveDevices(long activeDevices) {
        this.activeDevices = activeDevices;
    }

    public int getInactiveDevices() {
        return inactiveDevices;
    }

    public void setInactiveDevices(int inactiveDevices) {
        this.inactiveDevices = inactiveDevices;
    }

    public double getTotalConsumptionKWh() {
        return totalConsumptionKWh;
    }

    public void setTotalConsumptionKWh(double totalConsumptionKWh) {
        this.totalConsumptionKWh = totalConsumptionKWh;
    }

    public double getTotalCostUSD() {
        return totalCostUSD;
    }

    public void setTotalCostUSD(double totalCostUSD) {
        this.totalCostUSD = totalCostUSD;
    }

    public int getEfficiencyPct() {
        return efficiencyPct;
    }

    public void setEfficiencyPct(int efficiencyPct) {
        this.efficiencyPct = efficiencyPct;
    }

    public double getEstimatedMonthlyCost() {
        return estimatedMonthlyCost;
    }

    public void setEstimatedMonthlyCost(double estimatedMonthlyCost) {
        this.estimatedMonthlyCost = estimatedMonthlyCost;
    }

    public double getMonthlySavings() {
        return monthlySavings;
    }

    public void setMonthlySavings(double monthlySavings) {
        this.monthlySavings = monthlySavings;
    }

    public ComparisonResponse getComparison() {
        return comparison;
    }

    public void setComparison(ComparisonResponse comparison) {
        this.comparison = comparison;
    }

    public static class ComparisonResponse {

        private int consumptionPct;
        private int costPct;
        private int efficiencyPct;

        public int getConsumptionPct() {
            return consumptionPct;
        }

        public void setConsumptionPct(int consumptionPct) {
            this.consumptionPct = consumptionPct;
        }

        public int getCostPct() {
            return costPct;
        }

        public void setCostPct(int costPct) {
            this.costPct = costPct;
        }

        public int getEfficiencyPct() {
            return efficiencyPct;
        }

        public void setEfficiencyPct(int efficiencyPct) {
            this.efficiencyPct = efficiencyPct;
        }
    }
}

