package org.example.payroll;

import org.example.common.date_intervals.MonthInterval;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Расчетный период
 * @param payrollMonthInterval
 */
public record PayrollPeriod(MonthInterval payrollMonthInterval) {
    @Override
    public String toString() {
        YearMonth start = payrollMonthInterval.start();
        YearMonth end = payrollMonthInterval.end();
        String startMonth = start.format(DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru", "RU")));
        String endMonth = end.format(DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("ru", "RU")));

        if (payrollMonthInterval.start().equals(payrollMonthInterval.end())) {
            return startMonth;
        } else {
            return startMonth + " - " + endMonth;
        }

    }
}
