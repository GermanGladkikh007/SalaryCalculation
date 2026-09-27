import org.example.calculation.accural.NightWorkPayment;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.NightRate;
import org.example.employee.Employee;
import org.example.employee.NightShiftRateHistory;
import org.example.employee.SalaryHistory;
import org.example.period.NightPayrollPeriods;
import org.example.period.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NightPaymentTest {

    private Employee createEmployee(SalaryHistory salaryHistory, NightShiftRateHistory nightShiftRateHistory) {
        return new Employee(15, "Киллиан", "Мбаппе", salaryHistory, nightShiftRateHistory);
    }

    @Test
    void shouldCalculateForOneNight() {
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 1, 31)
                ),
                new NightRate(new Money(200000)));

        NightPayrollPeriods nightPayrollPeriods = new NightPayrollPeriods();
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                    LocalDate.of(2001, 1, 15),
                    LocalDate.of(2001, 1, 15)
                )
        );

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)));
        Employee employee = createEmployee(new SalaryHistory(), nightShiftRateHistory);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod, nightPayrollPeriods);
        Money result = nightWorkPayment.calculate();

        assertEquals(200000, result.kopecks());
    }

    @Test
    void shouldCalculateForSeveralNights() {
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 1, 31)
                ),
                new NightRate(new Money(200000)));

        NightPayrollPeriods nightPayrollPeriods = new NightPayrollPeriods();
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 1, 15),
                LocalDate.of(2001, 1, 17)));
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 1, 3),
                LocalDate.of(2001, 1, 6)));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 31)));
        Employee employee = createEmployee(new SalaryHistory(), nightShiftRateHistory);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod, nightPayrollPeriods);
        Money result = nightWorkPayment.calculate();

        assertEquals(1400000, result.kopecks());
    }

    @Test
    void shouldCalculateForNightAcrossMonths() {

        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 2, 28)
                ),
                new NightRate(new Money(200000)));

        NightPayrollPeriods nightPayrollPeriods = new NightPayrollPeriods();
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 1, 29),
                LocalDate.of(2001, 2, 3)));
        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 2, 28)));
        Employee employee = createEmployee(new SalaryHistory(), nightShiftRateHistory);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod, nightPayrollPeriods);
        Money result = nightWorkPayment.calculate();

        assertEquals(1200000, result.kopecks());
    }

    @Test
    void shouldCalculateForSeveralNightRates() {
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 2, 10)
                ),
                new NightRate(new Money(200000)));
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 2, 11),
                        LocalDate.of(2001, 3, 30)
                ),
                new NightRate(new Money(100000)));

        NightPayrollPeriods nightPayrollPeriods = new NightPayrollPeriods();
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 1, 29),
                LocalDate.of(2001, 2, 3)));
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 2, 28),
                LocalDate.of(2001, 3, 3)));

        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 3, 30)));
        Employee employee = createEmployee(new SalaryHistory(), nightShiftRateHistory);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod, nightPayrollPeriods);
        Money result = nightWorkPayment.calculate();

        assertEquals(1600000, result.kopecks());
    }

    @Test
    void shouldCalculateOnlyNightWorkWithinPayrollPeriod(){
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 2, 10)
                ),
                new NightRate(new Money(200000)));
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 2, 11),
                        LocalDate.of(2001, 3, 30)
                ),
                new NightRate(new Money(100000)));

        NightPayrollPeriods nightPayrollPeriods = new NightPayrollPeriods();
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 2, 28),
                LocalDate.of(2001, 3, 3)));
        nightPayrollPeriods.addNightPayrollPeriod(new DateInterval(
                LocalDate.of(2001, 1, 29),
                LocalDate.of(2001, 2, 3))
        );

        // расчетный период, интересующий нас не должен содержать ночных смен
        PayrollPeriod payrollPeriod = new PayrollPeriod(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 25)));
        Employee employee = createEmployee(new SalaryHistory(), nightShiftRateHistory);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod, nightPayrollPeriods);
        Money result = nightWorkPayment.calculate();

        assertEquals(0, result.kopecks());
    }
}
