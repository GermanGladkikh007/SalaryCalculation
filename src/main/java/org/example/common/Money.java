package org.example.common;

/**
 * Отдельный класс для удобства работы
 * с денежной единицой, поскольку по условию
 * все в целых копейках
 * @param kopecks
 */
public record Money(long kopecks) {
    public static final Money ZERO = new Money(0);

    public Money{
        if(kopecks < 0){
            throw new IllegalArgumentException("Денежная сумма не может быть отрицательной");
        }
    }

    public Money add(Money other) {
        return new Money(kopecks + other.kopecks);
    }

    public Money subtract(Money other) {
        return new Money(kopecks - other.kopecks);
    }

    public Money multiply(long multiplier) {
        return new Money(kopecks * multiplier);
    }

    public Money divide(long divisor) {
        if(divisor == 0){
            throw new ArithmeticException("Деление на ноль");
        }
        return new Money(kopecks / divisor);
    }
}
