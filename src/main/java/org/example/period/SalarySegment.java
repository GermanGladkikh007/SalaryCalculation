package org.example.period;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;

import java.time.LocalDate;

/**
 * В случае, когда в расчетный период
 * попадают интервалы дат с разными ставками
 * мы должны "раздробить на расчетный период"
 * на расчетные сегменты, для которых и есть этот класс
 * @param payrollDataInterval
 * @param salaryRate
 */
public record SalarySegment(DateInterval payrollDataInterval, SalaryRate salaryRate) {
    public void printSalarySegment() {
        LocalDate start = payrollDataInterval.start();
        LocalDate end = payrollDataInterval.end();
        System.out.println(start.toString() + " - " + end.toString() + " - " + salaryRate.toString());
    }
}
