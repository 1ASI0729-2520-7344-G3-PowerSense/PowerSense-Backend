package com.powersense.inventory.scheduling.domain.exceptions;

public class InvalidRuleConditionException extends DomainException {
    public InvalidRuleConditionException(String message) {
        super("Invalid rule condition: " + message);
    }
}
