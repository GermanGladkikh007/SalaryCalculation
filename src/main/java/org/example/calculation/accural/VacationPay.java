package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.date_intervals.DateInterval;
import org.example.common.Money;
import org.example.employee.Employee;
import org.example.employee.SalaryHistory;
import org.example.employee.WorkedDaysHistory;
import org.example.payroll.PayrollPeriod;
import org.example.period.VacationCalculationSegment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Начисление отпускных сотруднику за расчётный период.
 * Отпускные рассчитывается на основании истории изменения оклада сотрудника.
 * Интервалы действия окладов сопоставляются с расчётным периодом,
 * Затем сопоставляются с интервалами дат отпусков
 * после чего разбиваются по месяцам для пропорционального расчёта.
 * При расчете отпускных берем 0.9 оклада
 */
public class VacationPay implements Accrual {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;
    private Money vacationPay = Money.ZERO;


    private static final long VACATION_PAY_RATE_NUMERATOR = 9;
    private static final long VACATION_PAY_RATE_DENOMINATOR = 10;

    public VacationPay(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getVacationPay() {return vacationPay;}

    /**
     * Делим на сегменты ночных выплат, попадающих и в расчетный период и в интервалы дат отпусков
     * @return список сегментов, в котором каждому сегменту соответсвует интервал и ставка оклада на нем
     */
    private List<VacationCalculationSegment> getVacationCalculationSegments() {
        List<VacationCalculationSegment> result = new ArrayList<>();
        SalaryHistory salaryHistory = employee.salaryHistory();
        DateInterval payrollDateInterval = payrollPeriod.payrollMonthInterval().toDateInterval();
        WorkedDaysHistory workedDaysHistory = employee.workedDaysHistory();
        salaryHistory.forEachMatching(
                salaryInterval -> salaryInterval.overlaps(payrollDateInterval),
                (salaryInterval, salaryRate) -> {
                    DateInterval actualSalaryInterval =
                            salaryInterval.intersection(payrollDateInterval);

                    workedDaysHistory.vacationForEachMatching(
                            workedInterval -> workedInterval.overlaps(actualSalaryInterval),
                            (workedInterval) -> result.add(new VacationCalculationSegment(
                                    workedInterval.intersection(actualSalaryInterval),
                                    salaryRate
                            ))
                    );

                }
        );

        return result;
    }

    /**
     * Разделение уже получившихся сегментов по месяцам, чтобы
     * каждый сегмент был частью определенного месяца
     * @param vacationCalculationSegments
     * @return список сегментов, в котором каждому сегменту соответсвует интервал в границе
     * одного месяца и ставка оклада на этом интервале
     */
    private List<VacationCalculationSegment> splitByMonth(List<VacationCalculationSegment> vacationCalculationSegments) {
        List<VacationCalculationSegment> result = new ArrayList<>();

        for(VacationCalculationSegment vacationCalculationSegment : vacationCalculationSegments){
            LocalDate start = vacationCalculationSegment.payrollDateInterval().start();
            LocalDate end = vacationCalculationSegment.payrollDateInterval().end();

            LocalDate currentStart = start;

            while (!currentStart.isAfter(end)) {
                LocalDate endOfMonth = currentStart.withDayOfMonth(currentStart.lengthOfMonth());

                LocalDate currentEnd = endOfMonth.isBefore(end) ? endOfMonth : end;

                DateInterval dateInterval = new DateInterval(currentStart, currentEnd);

                result.add(new VacationCalculationSegment(dateInterval, vacationCalculationSegment.salaryRate()));

                currentStart = currentEnd.plusDays(1);
            }
        }

        return result;
    }
    @Override
    public Money calculate() {
        List<VacationCalculationSegment> splittedByMonthCalculationSegments = splitByMonth(getVacationCalculationSegments());
        Money vacation = Money.ZERO;

        for(VacationCalculationSegment vacationCalculationSegment : splittedByMonthCalculationSegments){
            Money vacationSegmentSalaryAmount = vacationCalculationSegment.salaryRate().salaryRate();
            long days = vacationCalculationSegment.payrollDateInterval().getDays();
            long monthDays = vacationCalculationSegment.payrollDateInterval().start().lengthOfMonth();
            vacation = vacation.add(vacationSegmentSalaryAmount.multiply(days)
                    .multiply(VACATION_PAY_RATE_NUMERATOR)
                    .divide(monthDays)
                    .divide(VACATION_PAY_RATE_DENOMINATOR)
            );

        }
        vacationPay = vacation;
        return vacation;
    }

    @Override
    public String getType() {
        return "Оплата отпуска";
    }

    @Override
    public String getAmount() {
        return Long.toString(vacationPay.kopecks());
    }
}
