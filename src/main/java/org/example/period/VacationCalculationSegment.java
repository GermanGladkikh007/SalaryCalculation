package org.example.period;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;

/** Расчетный сегмент для отпускных
 * @param payrollDateInterval
 * @param salaryRate
 */
public record VacationCalculationSegment(DateInterval payrollDateInterval, SalaryRate salaryRate) {
}
