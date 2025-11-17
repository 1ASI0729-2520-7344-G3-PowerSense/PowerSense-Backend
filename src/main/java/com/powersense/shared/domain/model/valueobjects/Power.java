package com.powersense.shared.domain.model.valueobjects;

import com.powersense.shared.domain.exceptions.ValidationException;

import java.text.NumberFormat;
import java.util.Locale;

public final class Power {
    private final int watts;

    private Power(int watts) {
        if (watts <= 0) throw new ValidationException("Watts must be greater than 0");
        this.watts = watts;
    }

    public static Power ofWatts(int watts) {
        return new Power(watts);
    }

    public static Power ofKilowatts(double kW) {
        if (kW <= 0) throw new ValidationException("Kilowatts must be greater than 0");
        int watts = (int) Math.round(kW * 1000.0);
        return new Power(watts);
    }

    public int getWatts() {
        return watts;
    }

    public double getKilowatts() {
        return watts / 1000.0;
    }

    public Energy toEnergyForHours(double hours) {
        if (hours < 0.0) throw new ValidationException("Hours cannot be negative");
        double kWh = getKilowatts() * hours;
        return Energy.ofKWh(kWh);
    }

    public String toFormattedString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setGroupingUsed(true);
        return nf.format(watts) + " W";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Power power)) return false;
        return watts == power.watts;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(watts);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
