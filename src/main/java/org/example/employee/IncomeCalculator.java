package org.example.employee;

import org.example.calculation.accural.*;
import org.example.common.Money;
import org.example.common.date_intervals.MonthInterval;
import org.example.payroll.PayrollPeriod;

import java.time.Month;
import java.time.YearMonth;

/**
 * Класс для расчета доходов в отдельные периоды(без удержаний)
 */
public class IncomeCalculator {

    private final Employee employee;

    public IncomeCalculator(Employee employee) {
        this.employee = employee;
    }

    /**
     * Доход с начала года до месяца,
     * который идет перед искомым месяцем
     * @param yearMonth
     * @return
     */
    public Money getIncomeBefore(YearMonth yearMonth) {
        Money incomeBefore = Money.ZERO;
        if(yearMonth.getMonth() == Month.JANUARY) {return incomeBefore;}

        YearMonth start = YearMonth.of(yearMonth.getYear(), 1);
        YearMonth end = yearMonth.minusMonths(1);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(start, end));
        Salary salary = new Salary(employee, payrollPeriod);
        VacationPay vacationPay = new VacationPay(employee, payrollPeriod);
        Bonus bonus = new Bonus(employee, payrollPeriod);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);
        SickLeavePay sickLeavePay = new SickLeavePay(employee, payrollPeriod);

        incomeBefore = incomeBefore.
                add(salary.calculate()).
                add(vacationPay.calculate()).
                add(bonus.calculate()).
                add(sickLeavePay.calculate()).
                add(nightWorkPayment.calculate());

        return incomeBefore;
    }

    /**
     * Доход за искомый месяц
     * @param yearMonth
     * @return
     */
    public Money getIncomeMonth(YearMonth yearMonth) {
        Money incomeBefore = Money.ZERO;

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(yearMonth, yearMonth));
        Salary salary = new Salary(employee, payrollPeriod);
        VacationPay vacationPay = new VacationPay(employee, payrollPeriod);
        Bonus bonus = new Bonus(employee, payrollPeriod);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);
        SickLeavePay sickLeavePay = new SickLeavePay(employee, payrollPeriod);

        incomeBefore = incomeBefore.
                add(salary.calculate()).
                add(vacationPay.calculate()).
                add(bonus.calculate()).
                add(sickLeavePay.calculate()).
                add(nightWorkPayment.calculate());

        return incomeBefore;
    }

}
