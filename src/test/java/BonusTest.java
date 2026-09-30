import org.example.calculation.accural.Bonus;
import org.example.common.*;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.BonusRate;
import org.example.common.rates.SalaryRate;
import org.example.employee.*;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BonusTest {

    EmployeeCreator employeeCreator = new EmployeeCreator();
    @Test
    void shouldCalculateBonusForOneMonth(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(90));
        Employee employee = employeeCreator.createEmployee(salaryHistory,bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new Bonus(employee,payrollPeriod).calculate();

        assertEquals(9000000, result.kopecks());
    }

    @Test
    void shouldCalculateBonusWithDifferentRatesForDifferentMonths(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 3, 31)),
                new SalaryRate(new Money(10000000)));

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(10));
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,3), YearMonth.of(2001,3)),new BonusRate(5));

        Employee employee = employeeCreator.createEmployee(salaryHistory,bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,3)));

        Money result = new Bonus(employee,payrollPeriod).calculate();

        assertEquals(1500000, result.kopecks());
    }

    @Test
    void shouldCalculateForSeveralMonths(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 3, 31)),
                new SalaryRate(new Money(10000000)));

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(10));
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,3), YearMonth.of(2001,3)),new BonusRate(10));

        Employee employee = employeeCreator.createEmployee(salaryHistory,bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,3)));

        Money result = new Bonus(employee,payrollPeriod).calculate();

        assertEquals(2000000, result.kopecks());
    }

    @Test
    void shouldIgnoreMonthsWithoutBonus(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 3, 31)),
                new SalaryRate(new Money(10000000)));

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(10));
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,3), YearMonth.of(2001,3)),new BonusRate(10));

        Employee employee = employeeCreator.createEmployee(salaryHistory,bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,2), YearMonth.of(2001,2)));

        Money result = new Bonus(employee,payrollPeriod).calculate();

        assertEquals(0, result.kopecks());
    }

    @Test
    void shouldCalculateBonusWithChangedSalary(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 15)),
                new SalaryRate(new Money(10000000)));
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 16), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(20000000)));

        BonusHistory bonusHistory = new BonusHistory();
        bonusHistory.addBonusRate(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)),new BonusRate(10));

        Employee employee = employeeCreator.createEmployee(salaryHistory,bonusHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new Bonus(employee,payrollPeriod).calculate();

        assertEquals(1516128, result.kopecks());
    }
}
