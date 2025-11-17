package com.powersense.shared.domain.model.valueobjects;

import com.powersense.shared.domain.exceptions.ValidationException;

import java.text.NumberFormat;
import java.util.Locale;

public final class Energy {
    private final double kWh;

    private Energy(double kWh) {
        if (Double.isNaN(kWh) || kWh < 0.0) {
            throw new ValidationException("Energy (kWh) cannot be negative");
        }
        this.kWh = kWh;
    }

    public static Energy ofKWh(double kWh) {
        return new Energy(kWh);
    }

    public static Energy ofWh(double wh) {
        if (Double.isNaN(wh) || wh < 0.0) {
            throw new ValidationException("Energy (Wh) cannot be negative");
        }
        return new Energy(wh / 1000.0);
    }

    public static Energy zero() {
        return new Energy(0.0);
    }

    public double getKWh() {
        return kWh;
    }

    public double getWh() {
        return kWh * 1000.0;
    }

    public Energy add(Energy other) {
        return new Energy(this.kWh + other.kWh);
    }

    public Energy subtract(Energy other) {
        double res = this.kWh - other.kWh;
        if (res < 0.0) throw new ValidationException("Resulting energy cannot be negative");
        return new Energy(res);
    }

    public Energy divide(double divisor) {
        if (divisor == 0.0) throw new ValidationException("Division by zero");
        return new Energy(this.kWh / divisor);
    }

    public boolean isGreaterThan(Energy other) {
        return this.kWh > other.kWh;
    }

    public boolean isZero() {
        return this.kWh == 0.0;
    }

    public String toFormattedString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(true);
        return nf.format(Math.round(kWh)) + " kWh";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Energy energy)) return false;
        return Double.compare(energy.kWh, kWh) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(kWh);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
