package com.powersense.inventory.scheduling.domain.exceptions;

public class RuleNotFoundException extends DomainException {
    public RuleNotFoundException(String ruleId) {
        super("Schedule rule not found: " + ruleId);
    }
}
