package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.InsuranceExperience;
import org.example.common.Money;
import org.example.common.date_intervals.DateInterval;
import org.example.employee.BonusHistory;
import org.example.employee.Employee;
import org.example.employee.SalaryHistory;
import org.example.employee.WorkedDaysHistory;
import org.example.period.BonusCalculationSegment;
import org.example.period.PayrollPeriod;
import org.example.period.SickLeavePaymentSegment;
import org.example.period.VacationCalculationSegment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Начисление больничных сотруднику за расчётный период.
 * Больничные рассчитывается на основании истории изменения оклада сотрудника.
 * Интервалы действия окладов сопоставляются с расчётным периодом,
 * Затем сопоставляются с интервалами дат болезни
 * после чего разбиваются по месяцам для пропорционального расчёта.
 * При расчете больничных берем процент от оклада в зависимости от стажа
 */
public class SickLeavePay implements Accrual {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;
    private Money payrollPeriodSickLeavePayment = Money.ZERO;

    public SickLeavePay(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getPayrollPeriodBonus() {
        return payrollPeriodSickLeavePayment;
    }

    /**
     * Делим на сегменты расчета больничного.
     * Берем пересечение интервалов дат со ставкой оклада, расчетным периодом
     * и интервалов дат с больничным
     * @return
     */
    private List<SickLeavePaymentSegment> getSickLeaveCalculationSegments() {
        List<SickLeavePaymentSegment> result = new ArrayList<>();
        SalaryHistory salaryHistory = employee.salaryHistory();
        DateInterval payrollDateInterval = payrollPeriod.payrollMonthInterval().toDateInterval();
        WorkedDaysHistory workedDaysHistory = employee.workedDaysHistory();
        salaryHistory.forEachMatching(
                salaryInterval -> salaryInterval.overlaps(payrollDateInterval),
                (salaryInterval, salaryRate) ->{
                    DateInterval actualSalaryInterval =
                            salaryInterval.intersection(payrollDateInterval);

                    workedDaysHistory.sickLeaveForEachMatching(
                            sickLeaveInterval -> sickLeaveInterval.overlaps(actualSalaryInterval),
                            (sickLeaveInterval) ->{

                                result.add(
                                        new SickLeavePaymentSegment(sickLeaveInterval.intersection(actualSalaryInterval),salaryRate)
                                );
                            }
                    );

                }
        );
        return result;
    }

    /**
     * Разделение уже получившихся сегментов по месяцам, чтобы
     * каждый сегмент был частью определенного месяца
     * @param sickLeavePaymentSegments
     * @return список сегментов, в котором каждому сегменту соответсвует интервал в границе
     * одного месяца и ставка оклада на этом интервале
     */
    private List<SickLeavePaymentSegment> splitByMonth(List<SickLeavePaymentSegment> sickLeavePaymentSegments) {
        List<SickLeavePaymentSegment> result = new ArrayList<>();

        for(SickLeavePaymentSegment sickLeavePaymentSegment : sickLeavePaymentSegments){
            LocalDate start = sickLeavePaymentSegment.dateInterval().start();
            LocalDate end = sickLeavePaymentSegment.dateInterval().end();

            LocalDate currentStart = start;

            while (!currentStart.isAfter(end)) {
                LocalDate endOfMonth = currentStart.withDayOfMonth(currentStart.lengthOfMonth());

                LocalDate currentEnd = endOfMonth.isBefore(end) ? endOfMonth : end;

                DateInterval dateInterval = new DateInterval(currentStart, currentEnd);

                result.add(new SickLeavePaymentSegment(dateInterval, sickLeavePaymentSegment.salaryRate()));

                currentStart = currentEnd.plusDays(1);
            }
        }

        return result;
    }


    @Override
    public Money calculate() {
        List<SickLeavePaymentSegment> splittedByMonthSickLeavePaymentSegment = splitByMonth(getSickLeaveCalculationSegments());

        Money sickLeavePay = Money.ZERO;

        long insuranceExperience = employee.insuranceExperience();
        long numerator; long denominator;
        if(insuranceExperience < 5){
            numerator = InsuranceExperience.LESS_THAN_5_YEARS.getNumerator();
            denominator = InsuranceExperience.LESS_THAN_5_YEARS.getDenominator();
        }else if(insuranceExperience < 8){
            numerator = InsuranceExperience.FROM_5_TO_8_YEARS.getNumerator();
            denominator = InsuranceExperience.FROM_5_TO_8_YEARS.getDenominator();
        }else{
            numerator = InsuranceExperience.MORE_THAN_8_YEARS.getNumerator();
            denominator = InsuranceExperience.MORE_THAN_8_YEARS.getDenominator();
        }

        for(SickLeavePaymentSegment sickLeavePaymentSegment : splittedByMonthSickLeavePaymentSegment){
            Money sickLeaveSegmentSalaryAmount = sickLeavePaymentSegment.salaryRate().salaryRate();
            long days = sickLeavePaymentSegment.dateInterval().getDays();
            long monthDays = sickLeavePaymentSegment.dateInterval().start().lengthOfMonth();
            sickLeavePay = sickLeavePay.add(sickLeaveSegmentSalaryAmount.multiply(days)
                    .multiply(numerator)
                    .divide(monthDays)
                    .divide(denominator)
            );

        }
        payrollPeriodSickLeavePayment = sickLeavePay;
        return sickLeavePay;
    }

    @Override
    public String getType() {
        return "Больничный";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodSickLeavePayment.kopecks());
    }
}
