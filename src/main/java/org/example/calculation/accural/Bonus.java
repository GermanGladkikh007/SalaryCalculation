package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.date_intervals.DateInterval;
import org.example.common.Money;
import org.example.employee.BonusHistory;
import org.example.employee.Employee;
import org.example.employee.SalaryHistory;
import org.example.period.BonusCalculationSegment;
import org.example.period.PayrollPeriod;

import java.util.ArrayList;
import java.util.List;

/**
 * Начисление премии за расчетный период
 * Берется процент от оклада в месяц и суммируются
 * получившиеся премии за расчетный период
 */
public class Bonus implements Accrual {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;
    private Money payrollPeriodBonus = Money.ZERO;

    private static final long BONUS_RATE_DENOMINATOR = 100;

    public Bonus(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getPayrollPeriodBonus() {
        return payrollPeriodBonus;
    }

    /**
     * Делим на сегменты расчета премии.
     * Берем пересечение интервалов дат со ставкой оклада, расчетным периодом
     * и интервалов дат с премией
     * @return
     */
    private List<BonusCalculationSegment> getBonusCalculationSegments() {
        List<BonusCalculationSegment> result = new ArrayList<>();
        BonusHistory bonusHistory = employee.bonusHistory();
        SalaryHistory salaryHistory = employee.salaryHistory();
        DateInterval payrollDateInterval = payrollPeriod.payrollMonthInterval().toDateInterval();
        salaryHistory.forEachMatching(
                salaryInterval -> salaryInterval.overlaps(payrollDateInterval),
                (salaryInterval, salaryRate) ->{
                    DateInterval actualSalaryInterval =
                            salaryInterval.intersection(payrollDateInterval);

                    bonusHistory.forEachMatching(
                            bonusInterval -> bonusInterval.overlaps(actualSalaryInterval),
                            (bonusInterval, bonusRate) ->{

                                result.add(
                                        new BonusCalculationSegment(bonusInterval.intersection(actualSalaryInterval),salaryRate,bonusRate)
                                );
                            }
                    );

                }
        );
        return result;
    }


    @Override
    public Money calculate() {
        List<BonusCalculationSegment> bonusCalculationSegments = getBonusCalculationSegments();
        Money bonus = Money.ZERO;

        for(BonusCalculationSegment bonusSegment : bonusCalculationSegments){
            Money bonusSegmentSalaryAmount = bonusSegment.salaryRate().salaryRate();
            long days = bonusSegment.dateInterval().getDays();
            long monthDays = bonusSegment.dateInterval().start().lengthOfMonth();
            long bonusRate = bonusSegment.bonusRate().percent();
            bonus = bonus.add(bonusSegmentSalaryAmount.multiply(days).multiply(bonusRate).divide(BONUS_RATE_DENOMINATOR).divide(monthDays));
        }

        payrollPeriodBonus = bonus;
        return bonus;
    }

    @Override
    public String getType() {
        return "Премия";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodBonus.kopecks());
    }
}
