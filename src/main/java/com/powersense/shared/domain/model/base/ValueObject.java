package com.powersense.shared.domain.model.base;

public abstract class ValueObject {

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
