package org.example.employee;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;
import org.example.exception.OverlappingDateIntervalException;

import java.util.*;

public class SalaryHistory {

    private NavigableMap<DateInterval, SalaryRate> salaryHistory = new TreeMap<>();

    public SalaryHistory(TreeMap<DateInterval, SalaryRate> salaryHistory) {
        this.salaryHistory = salaryHistory;
    }

    /**
     * Добавление интервала дат с новой ставкой оклада
     *
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

        salaryHistory.put(dateInterval, salaryRate);
    }

    public NavigableMap<DateInterval, SalaryRate> getSalaryHistory() {
        return salaryHistory;
    }

}
