package org.example.employee;

import org.example.common.DateInterval;
import org.example.common.NightRate;
import org.example.exception.NonContinuousDateIntervalException;
import org.example.exception.OverlappingDateIntervalException;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Абсолютный аналог SalaryHistory,
 * Также храним ставку ночных смен
 * на интервалах дат
 */
public class NightShiftRateHistory {
    private final NavigableMap<DateInterval, NightRate> nightRateHistory;

    public NightShiftRateHistory() {
        nightRateHistory = new TreeMap<>();
    }

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

    public NavigableMap<DateInterval, NightRate> getNightRateHistory() {return nightRateHistory;}
}
