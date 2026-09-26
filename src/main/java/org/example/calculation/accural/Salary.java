package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.SalaryRate;
import org.example.employee.Employee;
import org.example.period.PayrollPeriod;
import org.example.period.SalarySegment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

/**
 * Начисление оклада сотруднику за расчётный период.
 * Оклад рассчитывается на основании истории изменения оклада сотрудника.
 *  Интервалы действия окладов сопоставляются с расчётным периодом,
 *  после чего разбиваются по месяцам для пропорционального расчёта.
 */
public class Salary implements Accrual {

    private Employee employee;
    private PayrollPeriod payrollPeriod;
    private Money payrollPeriodSalary = Money.ZERO;

    public Salary(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getPayrollPeriodSalary() {
        return payrollPeriodSalary;
    }

    /**
     * Формирует сегменты оклада, попадающие в расчётный период.
     * @return список сегментов оклада за расчётный период,
     *         разбитых по месяцам
     */
    private List<SalarySegment> getSalarySegments() {
        List<SalarySegment> salarySegments = new ArrayList<>();
        NavigableMap<DateInterval,SalaryRate> salaryHistoryMap = employee.salaryHistory().getSalaryHistory();

        for (var entry : salaryHistoryMap.entrySet()){
            DateInterval dateInterval = entry.getKey();
            SalaryRate salaryRate = entry.getValue();
            if(dateInterval.overlaps(payrollPeriod.payrollDataInterval())){
                salarySegments.add(newSalarySegment(dateInterval,payrollPeriod,salaryRate));
            }
        }
        return splitByMonth(salarySegments);
    }

    /**
     * если интервал из истории окладов пересекается с расчетным периодом
     * то добавляем новый сегмент оклада, основываясь на пересечениях интервалов
     * @param dateInterval
     * @param payrollPeriod
     * @param salaryRate
     * @return
     */
    private SalarySegment newSalarySegment(DateInterval dateInterval, PayrollPeriod payrollPeriod, SalaryRate salaryRate) {
        DateInterval payrollInterval = payrollPeriod.payrollDataInterval();
        LocalDate start = dateInterval.start().isAfter(payrollInterval.start())
                ? dateInterval.start()
                : payrollInterval.start();

        LocalDate end = dateInterval.end().isBefore(payrollInterval.end())
                ? dateInterval.end()
                : payrollInterval.end();

        return new SalarySegment(new DateInterval(start,end),salaryRate);
    }


    /**
     * оклад привязан к месяцу
     * формально мы не можем сказать
     * то оклад у работника с 15.01 по 5.02 100к рублей
     * правильно будет:
     * с 15.01 по 31.01 - 100к рублей
     * с 1.02 по 5.02 - 100к рублей
     * поэтому делим сегменты еще и по месяцам
     * @param salarySegments
     * @return
     */
    private List<SalarySegment> splitByMonth(List<SalarySegment> salarySegments) {
        List<SalarySegment> result = new ArrayList<>();

        for(SalarySegment salarySegment : salarySegments){
            LocalDate start = salarySegment.payrollDataInterval().start();
            LocalDate end = salarySegment.payrollDataInterval().end();

            LocalDate currentStart = start;

            while(!currentStart.isAfter(end)){
                LocalDate endOfMonth = currentStart.withDayOfMonth(currentStart.lengthOfMonth());

                LocalDate currentEnd = endOfMonth.isBefore(end) ? endOfMonth : end;

                DateInterval dateInterval = new DateInterval(currentStart,currentEnd);

                result.add(new SalarySegment(dateInterval, salarySegment.salaryRate()));

                currentStart = currentEnd.plusDays(1);
            }
        }

        return result;
    }

    @Override
    public Money calculate() {
        List<SalarySegment> salarySegments = getSalarySegments();
        Money salary = Money.ZERO;
        for(SalarySegment salarySegment : salarySegments ){
            Money salarySegmentAmount = salarySegment.salaryRate().salaryRate();
            long days = salarySegment.payrollDataInterval().getDays();
            long monthDays = salarySegment.payrollDataInterval().start().lengthOfMonth();
            salary = salary.add(salarySegmentAmount.divide(monthDays).multiply(days));
        }
        payrollPeriodSalary = salary;
        return salary;
    }

    @Override
    public String getType() {
        return "Salary";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodSalary.kopecks());
    }

}
