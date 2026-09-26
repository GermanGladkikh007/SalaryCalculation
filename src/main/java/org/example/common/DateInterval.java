package org.example.common;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/**
 * Класс для хранения интервалов дат
 * @param start
 * @param end
 */
public record DateInterval(LocalDate start, LocalDate end) implements Comparable<DateInterval> {

    public DateInterval {
        if(start.isAfter(end)) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");
        }
    }

    @Override
    public int compareTo(DateInterval otherDateInterval) {
        return this.start.compareTo(otherDateInterval.start);
    }

    /**
     * Проверка пересечений интервалов дат
     * @param otherDateInterval
     * @return true, если интервалы пересекаются
     */
    public boolean overlaps(DateInterval otherDateInterval) {
        return !start.isAfter(otherDateInterval.end())
                &&
                !end.isBefore(otherDateInterval.start());
    }

    public long getDays() {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

}
