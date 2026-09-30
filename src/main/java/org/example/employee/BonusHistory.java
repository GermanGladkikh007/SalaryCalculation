package org.example.employee;

import org.example.common.rates.BonusRate;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.exception.OverlappingDateIntervalException;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Хранение истории процентных ставок премии
 */
public class BonusHistory {

    private NavigableMap<MonthInterval, BonusRate> bonusHistory;
    public BonusHistory() {bonusHistory = new TreeMap<>();}

    /**
     * Добавление нового интервала месяцев с процентной ставкой премии
     * @param monthInterval
     * @param bonusRate
     */
    public void addBonusRate(MonthInterval monthInterval, BonusRate bonusRate) {
        if(bonusHistory.isEmpty()) {
            bonusHistory.put(monthInterval, bonusRate);
            return;
        }

        MonthInterval previous = bonusHistory.floorKey(monthInterval);
        MonthInterval next = bonusHistory.ceilingKey(monthInterval);

        if(previous != null && previous.overlaps(monthInterval) || next != null && next.overlaps(monthInterval)){
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        bonusHistory.put(monthInterval, bonusRate);
    }

    /**
     * Защищенный проход по Map с условием
     * @param condition
     * @param action
     */
    public void forEachMatching(
            Predicate<DateInterval> condition,
            BiConsumer<DateInterval, BonusRate> action
    ) {
        for(var entry : bonusHistory.entrySet()){
            DateInterval dateInterval = entry.getKey().toDateInterval();
            BonusRate bonusRate = entry.getValue();

            if(condition.test(dateInterval)){
                action.accept(dateInterval, bonusRate);
            }
        }
    }
}
