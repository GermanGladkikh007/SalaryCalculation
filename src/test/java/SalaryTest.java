import org.example.calculation.accural.Salary;
import org.example.common.*;
import org.example.employee.*;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalaryTest {

    private EmployeeCreator employeeCreator = new EmployeeCreator();

    @Test
    void shouldCalculateSalaryForFullWorkedMonth() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                WorkDayStatus.WORKED);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new Salary(employee,payrollPeriod).calculate();

        assertEquals(10000000, result.kopecks());
    }

    @Test
    void shouldCalculateSalaryForPartWorkedMonth() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 3)),
                WorkDayStatus.SICK_LEAVE);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 4), LocalDate.of(2001, 1, 15)),
                WorkDayStatus.WORKED);
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 16), LocalDate.of(2001, 1, 31)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory());
        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new Salary(employee,payrollPeriod).calculate();

        assertEquals(3870967, result.kopecks());
    }

    @Test
    void shouldCalculatePartialMonthsAtPeriodBoundaries() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 2, 15)),
                new SalaryRate(new Money(10000000)));
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 2, 16), LocalDate.of(2001, 3, 31)),
                new SalaryRate(new Money(20000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 3, 31)),
                WorkDayStatus.WORKED);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory());
        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,3)));

        Money result = new Salary(employee,payrollPeriod).calculate();

        assertEquals(44642856, result.kopecks());
    }

    @Test
    void shouldCalculateSalaryForPartialSalaryHistoryMonth() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 15), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                WorkDayStatus.WORKED);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory());
        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new Salary(employee,payrollPeriod).calculate();

        assertEquals(5483870, result.kopecks());
    }
}
