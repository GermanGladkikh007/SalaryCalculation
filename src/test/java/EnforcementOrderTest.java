import org.example.calculation.deduction.EnforcementOrder;
import org.example.calculation.deduction.Tax;
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

public class EnforcementOrderTest {

    private EmployeeCreator employeeCreator = new EmployeeCreator();

    // EnforcementOrderPercent = 15
    @Test
    void shouldCalculateEnforcementOrderAmount(){
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

        EnforcementOrder enforcementOrder = new EnforcementOrder(employee,new Tax(employee,payrollPeriod).calculate());
        Money result = enforcementOrder.calculate();
        assertEquals(5_580_000, result.kopecks());
    }
}
