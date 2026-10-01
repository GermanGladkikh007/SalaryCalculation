package org.example.common.rates;

/**
 * Класс для прогрессивной налоговой ставки
 */
public enum TaxRate {
    UP_TO_2_4_MILLION(2_400_000, 13,100),
    FROM_2_4_TO_5_MILLION(5_000_000, 15,100),
    FROM_5_TO_20_MILLION(20_000_000L, 18,100),
    FROM_20_TO_50_MILLION(50_000_000L, 20,100),
    OVER_50_MILLION(Long.MAX_VALUE, 22,100);

    private final long limitKopecks;
    private final long numerator;
    private final long denominator;

    TaxRate(long limitKopecks, long numerator, long denominator) {
        this.limitKopecks = limitKopecks;
        this.numerator = numerator;
        this.denominator = denominator;
    }
    public long getNumerator() {return numerator;}

    public long getDenominator() {return denominator;}
}
