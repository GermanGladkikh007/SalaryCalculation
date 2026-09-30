package org.example.employee;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.SalaryRate;
import org.example.exception.NonContinuousDateIntervalException;
import org.example.exception.OverlappingDateIntervalException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Хранение истории ставок оклада
 * Отсутсвует геттеры, чтобы не было возможности извне менять список
 * Для этого есть отдельные методы, позволяющие обращаться к Map извне
 */

public class SalaryHistory {

    private final NavigableMap<DateInterval, SalaryRate> salaryHistory;

    public SalaryHistory() {
        salaryHistory = new TreeMap<>();
    }

    /**
     * Добавление интервала дат с новой ставкой оклада
     * Обязательно проверяем здесь пересечение интервалов
     * и отсутствие разрывов у них, поскольку ставка не может просто исчезнуть
     * на какой-то срок
     * @param dateInterval
     * @param salaryRate
     */
    public void addSalaryRate(DateInterval dateInterval, SalaryRate salaryRate) {
        if(salaryHistory.isEmpty()){
            salaryHistory.put(dateInterval, salaryRate);
            return;
        }

        DateInterval previous = salaryHistory.floorKey(dateInterval);
        DateInterval next = salaryHistory.ceilingKey(dateInterval);

        if(previous != null && previous.overlaps(dateInterval) || next != null && next.overlaps(dateInterval)){
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        if(previous != null && !previous.end().equals(dateInterval.start().minusDays(1)) ||
                next != null && !next.start().equals(dateInterval.end().plusDays(1))){
            throw new NonContinuousDateIntervalException("Новый интервал дат, не должен иметь разрывов с другими интервалами");
        }


        salaryHistory.put(dateInterval, salaryRate);
    }


    /**
     * Защищенный проход по Map с условием
     * @param condition
     * @param action
     */
    public void forEachMatching(
            Predicate<DateInterval> condition,
            BiConsumer<DateInterval, SalaryRate> action
    ) {
        for(var entry : salaryHistory.entrySet()){
            DateInterval dateInterval = entry.getKey();
            SalaryRate salaryRate = entry.getValue();

            if(condition.test(dateInterval)){
                action.accept(dateInterval, salaryRate);
            }
        }
    }

}
