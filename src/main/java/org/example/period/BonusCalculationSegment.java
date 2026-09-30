package org.example.period;

import org.example.common.rates.BonusRate;
import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.SalaryRate;

/**
 * Расчетный сегмент для премии
 * @param dateInterval
 * @param salaryRate
 * @param bonusRate
 */
public record BonusCalculationSegment(DateInterval dateInterval, SalaryRate salaryRate, BonusRate bonusRate) {
}
