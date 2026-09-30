package org.example.common;

/**
 * Процент от оклада для расчета больничного
 * в зависимости от стажа сотрудника
 */
public enum InsuranceExperience {

    LESS_THAN_5_YEARS(6,10),
    FROM_5_TO_8_YEARS(8,10),
    MORE_THAN_8_YEARS(10,10);

    private final long numerator;
    private final long denominator;

    InsuranceExperience(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public long getNumerator() {return numerator;}

    public long getDenominator() {return denominator;}
}
