package com.powersense.shared.domain.exceptions;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entityName, String id) {
        super(String.format("%s with ID '%s' not found", entityName, id));
    }
}
