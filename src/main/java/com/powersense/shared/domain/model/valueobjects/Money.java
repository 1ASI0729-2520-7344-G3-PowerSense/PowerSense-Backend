package com.powersense.shared.domain.model.valueobjects;

import com.powersense.shared.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        if (amount == null) throw new ValidationException("Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new ValidationException("Amount cannot be negative");
        if (currency == null || currency.isBlank()) throw new ValidationException("Currency cannot be empty");
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase(Locale.ROOT);
    }

    public static Money of(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Resulting amount cannot be negative");
        }
        return new Money(result, this.currency);
    }

    public Money multiply(double factor) {
        BigDecimal result = this.amount.multiply(BigDecimal.valueOf(factor));
        return new Money(result, this.currency);
    }

    public Money divide(double divisor) {
        if (divisor == 0.0) throw new ValidationException("Division by zero");
        BigDecimal result = this.amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public String toFormattedString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        try {
            nf.setCurrency(Currency.getInstance(this.currency));
        } catch (IllegalArgumentException ignore) {
            // fallback to default currency symbol if unknown code
        }
        return nf.format(this.amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    private void ensureSameCurrency(Money other) {
        if (!Objects.equals(this.currency, other.currency)) {
            throw new ValidationException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money that)) return false;
        return amount.compareTo(that.amount) == 0 && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
