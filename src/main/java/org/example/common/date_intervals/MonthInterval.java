package org.example.common.date_intervals;

import java.time.YearMonth;

/**
 * Аналог DateInterval, только храним интервал по месяцам
 * @param start
 * @param end
 */
public record MonthInterval(YearMonth start, YearMonth end) implements Comparable<MonthInterval>{
    public DateInterval toDateInterval() {
        return new DateInterval(
            start.atDay(1),
            end.atEndOfMonth()
        );
    }

    public boolean overlaps(MonthInterval otherMonthInterval) {
        return !start.isAfter(otherMonthInterval.end())
                && !end.isBefore(otherMonthInterval.start());
    }

    @Override
    public int compareTo(MonthInterval o) {
        return this.start.compareTo(o.start);
    }
}
