import org.example.calculation.accural.NightWorkPayment;
import org.example.calculation.accural.Salary;
import org.example.calculation.deduction.Tax;
import org.example.common.Money;
import org.example.common.WorkDayStatus;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.BonusRate;
import org.example.common.rates.NightRate;
import org.example.common.rates.SalaryRate;
import org.example.employee.*;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxTest {

    private EmployeeCreator employeeCreator = new EmployeeCreator();

    @Test
    void shouldCalculateTaxAt13PercentWhenIncomeBelowFirstThreshold(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                new SalaryRate(new Money(10000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 4, 30)),
                WorkDayStatus.WORKED);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 6, 1), LocalDate.of(2001, 6, 30)),
                WorkDayStatus.WORKED);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 8, 1), LocalDate.of(2001, 12, 31)),
                WorkDayStatus.WORKED);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 5, 1), LocalDate.of(2001, 5, 31)),
                WorkDayStatus.VACATION);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 7, 1), LocalDate.of(2001, 7, 31)),
                WorkDayStatus.SICK_LEAVE);

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(10));
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,4), YearMonth.of(2001,4)),new BonusRate(10));

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 12, 31)
                ),
                new NightRate(new Money(200000)));

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();
        workedNightsHistory.addWorkedNight(new DateInterval(
                        LocalDate.of(2001, 1, 15),
                        LocalDate.of(2001, 1, 17)
                )
        );
        workedNightsHistory.addWorkedNight(new DateInterval(
                        LocalDate.of(2001, 1, 3),
                        LocalDate.of(2001, 1, 6)
                )

        );

        Employee employee = employeeCreator.createEmployee(10,salaryHistory,nightShiftRateHistory,workedDaysHistory, workedNightsHistory, bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,7)));

        Money result = new Tax(employee,payrollPeriod).calculate();

        assertEquals(9_412_000, result.kopecks());
    }

    @Test
    void shouldCalculateTaxWhenIncomeCrossesFirstThreshold(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                new SalaryRate(new Money(35000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                WorkDayStatus.WORKED);

        BonusHistory bonusHistory = new BonusHistory();

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();

        Employee employee = employeeCreator.createEmployee(10,salaryHistory,nightShiftRateHistory,workedDaysHistory, workedNightsHistory, bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,8)));

        Money result = new Tax(employee,payrollPeriod).calculate();

        assertEquals(37_200_000, result.kopecks());
    }

    @Test
    void shouldCalculateTaxAt15PercentWhenIncomeBetweenFirstAndSecondThreshold(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                new SalaryRate(new Money(60000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                WorkDayStatus.WORKED);

        BonusHistory bonusHistory = new BonusHistory();

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();

        Employee employee = employeeCreator.createEmployee(10,salaryHistory,nightShiftRateHistory,workedDaysHistory, workedNightsHistory, bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,9)));

        Money result = new Tax(employee,payrollPeriod).calculate();

        assertEquals(77_400_000, result.kopecks());
    }

    @Test
    void shouldCalculateTaxWhenIncomeIsExactlyAtTaxThreshold(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                new SalaryRate(new Money(30000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                WorkDayStatus.WORKED);

        BonusHistory bonusHistory = new BonusHistory();

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();

        Employee employee = employeeCreator.createEmployee(10,salaryHistory,nightShiftRateHistory,workedDaysHistory, workedNightsHistory, bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,8)));

        Money result = new Tax(employee,payrollPeriod).calculate();

        assertEquals(31_200_000, result.kopecks());
    }




    @Test
    void shouldSplitMonthlyIncomeBetweenTaxRatesWhenIncomeCrossesLimit(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                new SalaryRate(new Money(230000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 12, 31)),
                WorkDayStatus.WORKED);

        BonusHistory bonusHistory = new BonusHistory();

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();

        Employee employee = employeeCreator.createEmployee(10,salaryHistory,nightShiftRateHistory,workedDaysHistory, workedNightsHistory, bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,2)));

        Money result = new Tax(employee,payrollPeriod).calculate();

        assertEquals(64_200_000, result.kopecks());
    }
}
