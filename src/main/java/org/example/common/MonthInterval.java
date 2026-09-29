package org.example.common;

import java.time.YearMonth;

/**
 * Аналог DateInterval, только храним интервал по месяцам
 * @param start
 * @param end
 */
public record MonthInterval(YearMonth start, YearMonth end) {
    public DateInterval toDateInterval() {
        return new DateInterval(
            start.atDay(1),
            end.atEndOfMonth()
        );
    }
}
