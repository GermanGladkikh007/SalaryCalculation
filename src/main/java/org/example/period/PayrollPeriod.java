package org.example.period;

import org.example.common.DateInterval;

/**
 * Хранит информацию о искомом расчетном периоде
 * @param payrollDataInterval интервал дат расчетного периода
 */
public record PayrollPeriod(DateInterval payrollDataInterval) {

}
