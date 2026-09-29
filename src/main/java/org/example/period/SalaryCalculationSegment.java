package org.example.period;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;
import org.example.common.WorkDayStatus;
import org.example.employee.WorkedDaysHistory;

/** Расчетный сегмент для оклада
 * @param payrollDateInterval
 * @param salaryRate
 */
public record SalaryCalculationSegment(DateInterval payrollDateInterval, SalaryRate salaryRate) {

}
