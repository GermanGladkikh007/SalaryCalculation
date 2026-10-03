import org.example.calculation.accural.NightWorkPayment;
import org.example.common.date_intervals.DateInterval;
import org.example.common.Money;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.NightRate;
import org.example.employee.*;
import org.example.payroll.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NightPaymentTest {

    private EmployeeCreator employeeCreator = new EmployeeCreator();

    @Test
    void shouldCalculateForOneNight() {
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        nightShiftRateHistory.addNightRate(new DateInterval(
                        LocalDate.of(2001, 1, 1),
                        LocalDate.of(2001, 1, 31)
                ),
                new NightRate(new Money(200000)));

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();
        workedNightsHistory.addWorkedNight(new DateInterval(
                        LocalDate.of(2001, 1, 15),
                        LocalDate.of(2001, 1, 15)
                )
        );
        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001, 1), YearMonth.of(2001, 1)));
        Employee employee = employeeCreator.createEmployee(new SalaryHistory(), nightShiftRateHistory, new WorkedDaysHistory(), workedNightsHistory, new BonusHistory());
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);
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


        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001, 1), YearMonth.of(2001, 1)));
        Employee employee = employeeCreator.createEmployee(new SalaryHistory(), nightShiftRateHistory, new WorkedDaysHistory(), workedNightsHistory, new BonusHistory());
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);
        Money result = nightWorkPayment.calculate();

        assertEquals(1400000, result.kopecks());
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

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();
        workedNightsHistory.addWorkedNight(new DateInterval(
                    LocalDate.of(2001, 1, 29),
                    LocalDate.of(2001, 2, 3)
                )
        );
        workedNightsHistory.addWorkedNight(new DateInterval(
                    LocalDate.of(2001, 2, 28),
                    LocalDate.of(2001, 3, 3)
                )

        );

        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001, 1), YearMonth.of(2001, 3)));
        Employee employee = employeeCreator.createEmployee(new SalaryHistory(), nightShiftRateHistory, new WorkedDaysHistory(), workedNightsHistory, new BonusHistory());
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);

        Money result = nightWorkPayment.calculate();

        assertEquals(1600000, result.kopecks());
    }


    @Test
    void shouldCalculateOnlyNightWorkWithinPayrollPeriod() {
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

        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();
        workedNightsHistory.addWorkedNight(new DateInterval(
                        LocalDate.of(2001, 1, 29),
                        LocalDate.of(2001, 2, 3)
                )
        );

        // расчетный период, интересующий нас не должен содержать ночных смен
        PayrollPeriod payrollPeriod = new PayrollPeriod(new MonthInterval(YearMonth.of(2001, 3), YearMonth.of(2001, 3)));
        Employee employee = employeeCreator.createEmployee(new SalaryHistory(), nightShiftRateHistory, new WorkedDaysHistory(), workedNightsHistory, new BonusHistory());
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);

        Money result = nightWorkPayment.calculate();

        assertEquals(0, result.kopecks());
    }
}
