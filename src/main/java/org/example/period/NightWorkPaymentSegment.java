package org.example.period;

import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.NightRate;

import java.time.LocalDate;
import java.util.Date;

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
