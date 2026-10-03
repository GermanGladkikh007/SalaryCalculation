package org.example.employee;

public record EnforcementOrderPercent(long percent) {

    public EnforcementOrderPercent {
        if (percent < 0 || percent > 50) {
            throw new IllegalArgumentException(
                    "Процент по исполнительному листу не должен превышать 50%"
            );
        }
    }
}