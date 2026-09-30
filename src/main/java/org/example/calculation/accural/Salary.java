package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.date_intervals.DateInterval;
import org.example.common.Money;
import org.example.employee.Employee;
import org.example.employee.SalaryHistory;
import org.example.employee.WorkedDaysHistory;
import org.example.period.PayrollPeriod;
import org.example.period.SalaryCalculationSegment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Начисление оклада сотруднику за расчётный период.
 * Оклад рассчитывается на основании истории изменения оклада сотрудника.
 * Интервалы действия окладов сопоставляются с расчётным периодом,
 * после чего разбиваются по месяцам для пропорционального расчёта.
 */
public class Salary implements Accrual {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;
    private Money payrollPeriodSalary = Money.ZERO;

    public Salary(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getPayrollPeriodSalary() {
        return payrollPeriodSalary;
    }

    /**
     * Делим на сегменты ночных выплат, попадающих и в расчетный период и в интервалы выходов на ночные смены
     * @return список сегментов, в котором каждому сегменту соответсвует интервал и ставка в ночную смену на нем
     */
    private List<SalaryCalculationSegment> getSalaryCalculationSegments() {
        List<SalaryCalculationSegment> result = new ArrayList<>();
        WorkedDaysHistory workedDaysHistory = employee.workedDaysHistory();
        SalaryHistory salaryHistory = employee.salaryHistory();
        DateInterval payrollDateInterval = payrollPeriod.payrollMonthInterval().toDateInterval();
        salaryHistory.forEachMatching(
                salaryInterval -> salaryInterval.overlaps(payrollDateInterval),
                (salaryInterval, salaryRate) -> {
                        DateInterval actualSalaryInterval =
                                salaryInterval.intersection(payrollDateInterval);

                        workedDaysHistory.salaryForEachMatching(
                                workedInterval -> workedInterval.overlaps(actualSalaryInterval),
                                (workedInterval) -> result.add(new SalaryCalculationSegment(
                                        workedInterval.intersection(actualSalaryInterval),
                                        salaryRate
                                ))
                        );

                    }
                );

        return result;
    }


    /**
     * Оклад привязан к месяцу.
     * формально мы не можем сказать,
     * что оклад у работника с 15.01 по 31.02 100к рублей
     * правильно будет:
     * с 15.01 по 31.01 - 100к рублей
     * с 1.02 по 31.02 - 100к рублей;
     * поэтому делим сегменты еще и по месяцам
     * @param
     * @return List
     */
    private List<SalaryCalculationSegment> splitByMonth(List<SalaryCalculationSegment> SalaryCalculationSegments) {
        List<SalaryCalculationSegment> result = new ArrayList<>();

        for (SalaryCalculationSegment salaryCalculationSegment : SalaryCalculationSegments) {
            LocalDate start = salaryCalculationSegment.payrollDateInterval().start();
            LocalDate end = salaryCalculationSegment.payrollDateInterval().end();

            LocalDate currentStart = start;

            while (!currentStart.isAfter(end)) {
                LocalDate endOfMonth = currentStart.withDayOfMonth(currentStart.lengthOfMonth());

                LocalDate currentEnd = endOfMonth.isBefore(end) ? endOfMonth : end;

                DateInterval dateInterval = new DateInterval(currentStart, currentEnd);

                result.add(new SalaryCalculationSegment(dateInterval, salaryCalculationSegment.salaryRate()));

                currentStart = currentEnd.plusDays(1);
            }
        }

        return result;
    }


    @Override
    public Money calculate() {
        List<SalaryCalculationSegment> splittedByMonthCalculationSegments = splitByMonth(getSalaryCalculationSegments());
        Money salary = Money.ZERO;

        for(SalaryCalculationSegment salaryCalculationSegment : splittedByMonthCalculationSegments){
            Money salarySegmentAmount = salaryCalculationSegment.salaryRate().salaryRate();
            long days = salaryCalculationSegment.payrollDateInterval().getDays();
            long monthDays = salaryCalculationSegment.payrollDateInterval().start().lengthOfMonth();
            salary = salary.add(salarySegmentAmount.multiply(days).divide(monthDays));
        }
        payrollPeriodSalary = salary;
        return salary;
    }

    @Override
    public String getType() {
        return "Оплата по окладу";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodSalary.kopecks());
    }


}
