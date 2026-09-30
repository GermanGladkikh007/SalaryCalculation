package org.example.period;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.SalaryRate;

/** Расчетный сегмент для отпускных
 * @param payrollDateInterval
 * @param salaryRate
 */
public record VacationCalculationSegment(DateInterval payrollDateInterval, SalaryRate salaryRate) {
}
