import org.example.calculation.accural.Salary;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.SalaryRate;
import org.example.employee.Employee;
import org.example.employee.SalaryHistory;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SalaryTest {

    private Employee createEmployee(SalaryHistory salaryHistory) {
        return new Employee(15, "Киллиан", "Мбаппе", salaryHistory);
    }

    /**
     * Проверяет расчёт полного месячного оклада.
     */

    @Test
    void shouldCalculateSalaryForFullMonth() {
        TreeMap<DateInterval, SalaryRate> history = new TreeMap<>();

        history.put(new DateInterval(LocalDate.of(2001,1,1), LocalDate.of(2001,1,31)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(new SalaryHistory(history));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001,1,1), LocalDate.of(2001,1,31)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(10000000, result.kopecks());
    }


    /**
     * Проверяет пропорциональный расчёт оклада за неполный месяц.
     */
    @Test
    void shouldCalculateSalaryForPartOfMonth() {

        TreeMap<DateInterval, SalaryRate> history = new TreeMap<>();

        history.put(new DateInterval(LocalDate.of(2001,1,1), LocalDate.of(2001,2,28)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(new SalaryHistory(history));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001,1,10), LocalDate.of(2001,1,20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(3548387, result.kopecks());
    }


    /**
     * Проверяет расчёт оклада за расчётный период,
     * охватывающий несколько месяцев.
     */

    @Test
    void shouldCalculateSalaryForSeveralMonths() {
        TreeMap<DateInterval, SalaryRate> history = new TreeMap<>();

        history.put(new DateInterval(LocalDate.of(2001,1,1), LocalDate.of(2001,5,31)),
                new SalaryRate(new Money(10000000)));

        Employee employee = createEmployee(new SalaryHistory(history));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001,1,10), LocalDate.of(2001,4,20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(33763440, result.kopecks());
    }


    /**
     * Проверяет расчёт оклада при изменении ставки
     * внутри расчётного периода.
     */
    @Test
    void shouldCalculateSalaryWithRateChange(){
        TreeMap<DateInterval, SalaryRate> history = new TreeMap<>();

        history.put(new DateInterval(LocalDate.of(2001,1,1), LocalDate.of(2001,5,20)),
                new SalaryRate(new Money(10000000)));

        history.put(new DateInterval(LocalDate.of(2001,5,21), LocalDate.of(2001,8,31)),
                new SalaryRate(new Money(20000000)));
        Employee employee = createEmployee(new SalaryHistory(history));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001,3,10), LocalDate.of(2001,8,20)));

        Money result = new Salary(employee, payrollPeriod).calculate();

        assertEquals(83548385, result.kopecks());
    }

}
