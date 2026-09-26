package org.example.period;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;

/**
 * В случае, когда в расчетный период
 * попадают интервалы дат с разными ставками
 * мы должны "раздробить на расчетный период"
 * на расчетные сегменты, для которых и есть этот класс
 * @param payrollDataInterval
 * @param salaryRate
 */
public record SalarySegment(DateInterval payrollDataInterval, SalaryRate salaryRate) {

}
