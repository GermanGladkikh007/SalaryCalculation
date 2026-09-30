package org.example.period;

import org.example.common.date_intervals.DateInterval;
import org.example.common.rates.SalaryRate;
import org.example.employee.SalaryHistory;

/** Расчетный сегмент для больничного
 * @param dateInterval
 * @param salaryRate
 */
public record SickLeavePaymentSegment(DateInterval dateInterval, SalaryRate salaryRate) {
}
