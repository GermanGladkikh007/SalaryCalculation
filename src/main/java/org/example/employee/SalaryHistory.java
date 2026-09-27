package org.example.employee;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;
import org.example.exception.NonContinuousDateIntervalException;
import org.example.exception.OverlappingDateIntervalException;

import java.util.*;

public class SalaryHistory {

    private final NavigableMap<DateInterval, SalaryRate> salaryHistory;

    public SalaryHistory() {
        salaryHistory = new TreeMap<>();
    }

    /**
     * Добавление интервала дат с новой ставкой оклада
     * Обязательно проверяем здесь пересечение интервалов
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

    public NavigableMap<DateInterval, SalaryRate> getSalaryHistory() {
        return salaryHistory;
    }

}
