package org.example.common.rates;

/**
 * Класс для процента премии от оклада
 * @param percent
 */
public record BonusRate(long percent) {
    public BonusRate {
        if (percent < 0)  {
            throw new IllegalArgumentException("Процент премии не может быть отрицательным");
        }
        if(percent > 100){
            throw new IllegalArgumentException("Процент премии не может быть больше 100");
        }
    }
}
