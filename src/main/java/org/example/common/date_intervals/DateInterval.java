package org.example.common.date_intervals;

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

    /**
     * Получение нового интервала из пересечения двух
     * @param otherDateInterval
     * @return DateInterval
     */
    public DateInterval intersection(DateInterval otherDateInterval) {
        if(!overlaps(otherDateInterval)) {
            throw new IllegalArgumentException(
                    "Интервалы не пересекаются"
            );
        }

        LocalDate from = start.isAfter(otherDateInterval.start())
                ? start
                : otherDateInterval.start();

        LocalDate to = end.isBefore(otherDateInterval.end())
                ? end
                : otherDateInterval.end();

        return new DateInterval(from, to);
    }

    /**
     * Количество дней в интервале, включая его концы
     * @return long
     */
    public long getDays() {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

}
