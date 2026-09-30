package org.example.employee;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.NightRate;
import org.example.exception.NonContinuousDateIntervalException;
import org.example.exception.OverlappingDateIntervalException;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Хранение истории ставок в ночную смену
 * Отсутсвует геттеры, чтобы не было возможности извне менять список
 * Для этого есть отдельные методы, позволяющие обращаться к Map извне
 */
public class NightShiftRateHistory {
    private final NavigableMap<DateInterval, NightRate> nightRateHistory;

    public NightShiftRateHistory() {
        nightRateHistory = new TreeMap<>();
    }


    /**
     * Добавление нового интервала с новой ставкой в ночную смену
     * Обязательно проверяем здесь пересечение интервалов
     * и отсутствие разрывов у них, поскольку ставка не может просто исчезнуть
     * на какой-то срок
     * @param dateInterval
     * @param nightRate
     */
    public void addNightRate(DateInterval dateInterval, NightRate nightRate) {
        if(nightRateHistory.isEmpty()){
            nightRateHistory.put(dateInterval,nightRate);
            return;
        }

        DateInterval previous = nightRateHistory.floorKey(dateInterval);
        DateInterval next = nightRateHistory.ceilingKey(dateInterval);

        if(previous != null && previous.overlaps(dateInterval) || next != null && next.overlaps(dateInterval)){
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        if(previous != null && !previous.end().equals(dateInterval.start().minusDays(1)) ||
                next != null && !next.start().equals(dateInterval.end().plusDays(1))){
            throw new NonContinuousDateIntervalException("Новый интервал дат, не должен иметь разрывов с другими интервалами");
        }


        nightRateHistory.put(dateInterval, nightRate);
    }


    /**
     * Защищенный проход по Map с условием
     * @param condition
     * @param action
     */

    public void forEachMatching(Predicate<DateInterval> condition, BiConsumer<DateInterval, NightRate> action) {
        for(var entry : nightRateHistory.entrySet()) {
            DateInterval dateInterval = entry.getKey();
            NightRate nightRate = entry.getValue();
            if(condition.test(dateInterval)){
                action.accept(dateInterval, nightRate);
            }
        }

    }
}
