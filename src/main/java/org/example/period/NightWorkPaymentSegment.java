package org.example.period;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.NightRate;

import java.time.LocalDate;

/**
 * Расчетный сегмент для ночных смен
 * @param workedNights
 * @param nightRate
 */
public record NightWorkPaymentSegment(DateInterval workedNights, NightRate nightRate) {
    public void printNightWorkSegment() {
        LocalDate start = workedNights.start();
        LocalDate end = workedNights.end();
        System.out.println(start.toString() + " - " + end.toString() + " - " + nightRate.toString());
    }
}
