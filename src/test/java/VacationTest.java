import org.example.calculation.accural.VacationPay;
import org.example.common.*;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.SalaryRate;
import org.example.employee.*;
import org.example.payroll.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VacationTest {

    private EmployeeCreator employeeCreator = new EmployeeCreator();

    @Test
    void shouldCalculateVacationPayForFullMonth(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory(), new BonusHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new VacationPay(employee,payrollPeriod).calculate();

        assertEquals(9000000, result.kopecks());

    }

    @Test
    void shouldCalculateVacationPayForPartOfMonth(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 15), LocalDate.of(2001, 1, 31)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory(), new BonusHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new VacationPay(employee,payrollPeriod).calculate();

        assertEquals(4935483, result.kopecks());
    }

    @Test
    void shouldCalculateVacationPayForVacationAcrossSeveralMonths(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(10000000)));


        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 25), LocalDate.of(2001, 2, 5)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory(), new BonusHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,2)));

        Money result = new VacationPay(employee,payrollPeriod).calculate();

        assertEquals(3639400, result.kopecks());
    }

    @Test
    void shouldCalculateVacationPayWithDifferentSalaryRates(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 2, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(20000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 25), LocalDate.of(2001, 2, 5)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory(),new BonusHistory() );

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,2)));

        Money result = new VacationPay(employee,payrollPeriod).calculate();

        assertEquals(5246543, result.kopecks());
    }

    // Здесь отпуск выходит за расчетный период
    @Test
    void shouldCalculateVacationPayOnlyForVacationDaysWithinPayrollPeriod(){
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 2, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(20000000)));

        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        workedDaysHistory.addWorkedDaysStatus(new DateInterval(LocalDate.of(2001, 1, 25), LocalDate.of(2001, 2, 5)),
                WorkDayStatus.VACATION);

        Employee employee = employeeCreator.createEmployee(salaryHistory,new NightShiftRateHistory(),workedDaysHistory, new WorkedNightsHistory(), new BonusHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001,1), YearMonth.of(2001,1)));

        Money result = new VacationPay(employee,payrollPeriod).calculate();

        assertEquals(2032258, result.kopecks());
    }


}
