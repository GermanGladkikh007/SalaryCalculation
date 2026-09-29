package org.example.period;

import org.example.common.DateInterval;
import org.example.common.MonthInterval;

/** Расчетный период
 * @param payrollMonthInterval
 */
public record PayrollPeriod(MonthInterval payrollMonthInterval) {

}
