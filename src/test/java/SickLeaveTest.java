import org.example.calculation.accural.SickLeavePay;
import org.example.common.Money;
import org.example.common.WorkDayStatus;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.SalaryRate;
import org.example.employee.*;
import org.example.payroll.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SickLeaveTest {
    private EmployeeCreator employeeCreator = new EmployeeCreator();

    @Test
    void shouldCalculateSickLeaveForEmployeeWithLessThanFiveYearsExperience(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 15), LocalDate.of(2001, 1, 20)),
                WorkDayStatus.SICK_LEAVE);

        Employee employee = employeeCreator.createEmployee(4, salaryHistory, workedDaysHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new SickLeavePay(employee,payrollPeriod).calculate();
        assertEquals(1161290, result.kopecks());
    }

    @Test
    void shouldCalculateSickLeaveForEmployeeWithFiveToEightYearsExperience(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 15), LocalDate.of(2001, 1, 20)),
                WorkDayStatus.SICK_LEAVE);

        Employee employee = employeeCreator.createEmployee(6, salaryHistory, workedDaysHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new SickLeavePay(employee,payrollPeriod).calculate();
        assertEquals(1548387, result.kopecks());
    }

    @Test
    void shouldCalculateSickLeaveForEmployeeWithEightOrMoreYearsExperience(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 15), LocalDate.of(2001, 1, 20)),
                WorkDayStatus.SICK_LEAVE);

        Employee employee = employeeCreator.createEmployee(9, salaryHistory, workedDaysHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new SickLeavePay(employee,payrollPeriod).calculate();
        assertEquals(1935483, result.kopecks());
    }

    @Test
    void shouldCalculateSickLeaveForSeveralMonths(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 28), LocalDate.of(2001, 2, 2)),
                WorkDayStatus.SICK_LEAVE);

        Employee employee = employeeCreator.createEmployee(9, salaryHistory, workedDaysHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,2)));

        Money result = new SickLeavePay(employee,payrollPeriod).calculate();
        assertEquals(2004607, result.kopecks());
    }

    @Test
    void shouldCalculateSickLeaveWithChangedSalary(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1,31)),
                new SalaryRate(new Money(10000000)));
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 2, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(20000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 28), LocalDate.of(2001, 2, 2)),
                WorkDayStatus.SICK_LEAVE);

        Employee employee = employeeCreator.createEmployee(9, salaryHistory, workedDaysHistory);

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,2)));

        Money result = new SickLeavePay(employee,payrollPeriod).calculate();
        assertEquals(2718893, result.kopecks());
    }

}
