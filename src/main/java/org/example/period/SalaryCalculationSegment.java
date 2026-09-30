package org.example.period;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.SalaryRate;

/** Расчетный сегмент для оклада
 * @param payrollDateInterval
 * @param salaryRate
 */
public record SalaryCalculationSegment(DateInterval payrollDateInterval, SalaryRate salaryRate) {

}
