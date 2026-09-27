import org.example.calculation.accural.Salary;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.SalaryRate;
import org.example.employee.Employee;
import org.example.employee.NightShiftRateHistory;
import org.example.employee.SalaryHistory;
import org.example.exception.OverlappingDateIntervalException;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SalaryTest {

    private Employee createEmployee(SalaryHistory salaryHistory, NightShiftRateHistory nightShiftRateHistory) {
        return new Employee(15, "Киллиан", "Мбаппе", salaryHistory, nightShiftRateHistory);
    }

    /**
     * Проверяет расчёт полного месячного оклада.
     */

    @Test
    void shouldCalculateSalaryForFullMonth() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(salaryHistory, new NightShiftRateHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(10000000, result.kopecks());
    }


    /**
     * Проверяет пропорциональный расчёт оклада за неполный месяц.
     */
    @Test
    void shouldCalculateSalaryForPartOfMonth() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 2, 28)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(salaryHistory, new NightShiftRateHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 10), LocalDate.of(2001, 1, 20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(3548387, result.kopecks());
    }


    /**
     * Проверяет расчёт оклада за расчётный период,
     * охватывающий несколько месяцев.
     */

    @Test
    void shouldCalculateSalaryForSeveralMonths() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 5, 31)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(salaryHistory, new NightShiftRateHistory());

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 10), LocalDate.of(2001, 4, 20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(33763440, result.kopecks());
    }


    /**
     * Проверяет расчёт оклада при изменении ставки
     * внутри расчётного периода.
     */
    @Test
    void shouldCalculateSalaryWithRateChange() {
        SalaryHistory salaryHistory = new SalaryHistory();

        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 5, 20)),
                new SalaryRate(new Money(10000000)));

        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 5, 21), LocalDate.of(2001, 8, 31)),
                new SalaryRate(new Money(20000000)));

        Employee employee = createEmployee(salaryHistory, new NightShiftRateHistory());
        TreeMap<DateInterval, SalaryRate> history = new TreeMap<>();

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 3, 10), LocalDate.of(2001, 8, 20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(83548385, result.kopecks());
    }

}
