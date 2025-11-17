package com.powersense.shared.domain.model.valueobjects;

import com.powersense.shared.domain.exceptions.ValidationException;

public final class Percentage {
    private final double value; // 0..100

    private Percentage(double value) {
        if (Double.isNaN(value) || value < 0.0 || value > 100.0) {
            throw new ValidationException("Percentage must be between 0 and 100");
        }
        this.value = value;
    }

    public static Percentage of(double value) {
        return new Percentage(value);
    }

    public static Percentage fromDecimal(double decimal) {
        if (decimal < 0.0 || decimal > 1.0) {
            throw new ValidationException("Decimal must be between 0 and 1");
        }
        return new Percentage(decimal * 100.0);
    }

    public double getValue() {
        return value;
    }

    public double asDecimal() {
        return value / 100.0;
    }

    public boolean isPositive() {
        return value > 0.0;
    }

    public boolean isNegative() {
        return value < 0.0;
    }

    public boolean isZero() {
        return value == 0.0;
    }

    public String toFormattedString() {
        return String.format("%.0f%%", value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Percentage that)) return false;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
