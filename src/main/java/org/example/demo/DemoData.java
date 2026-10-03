package org.example.demo;

import org.example.common.Money;
import org.example.common.WorkDayStatus;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.BonusRate;
import org.example.common.rates.NightRate;
import org.example.common.rates.SalaryRate;
import org.example.employee.*;
import org.example.payroll.PayrollPeriod;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class DemoData {

    public static List<Employee> createEmployees() {
        return List.of(
                createSimpleEmployee(),
                createEmployeeWithBonus(),
                createEmployeeWithVacation(),
                createEmployeeWithNightAndSickLeave(),
                createFullEmployee()
        );
    }

    private static Employee createSimpleEmployee() {
        SalaryHistory salaryHistory = new SalaryHistory();
        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2024, 2, 12),
                        LocalDate.of(2026, 12, 31)
                ),
                new SalaryRate(new Money(15000000))
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2024, 2, 12),
                        LocalDate.of(2026, 12, 31)
                ),
                WorkDayStatus.WORKED
        );

        return new Employee(
                1, "Василий", "Иванов", 10,
                salaryHistory, workedDaysHistory
        );
    }
    private static Employee createEmployeeWithBonus() {
        SalaryHistory salaryHistory = new SalaryHistory();
        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        BonusHistory bonusHistory = new BonusHistory();

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2024, 3, 4),
                        LocalDate.of(2025, 8, 31)
                ),
                new SalaryRate(new Money(12000000))
        );

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2025, 9, 1),
                        LocalDate.of(2026, 12, 31)
                ),
                new SalaryRate(new Money(16000000))
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2024, 3, 4),
                        LocalDate.of(2025, 7, 13)
                ),
                WorkDayStatus.WORKED
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 7, 14),
                        LocalDate.of(2025, 7, 25)
                ),
                WorkDayStatus.VACATION
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 7, 26),
                        LocalDate.of(2026, 12, 31)
                ),
                WorkDayStatus.WORKED
        );

        bonusHistory.addBonusRate(
                new MonthInterval(
                        YearMonth.of(2024, 12),
                        YearMonth.of(2024, 12)
                ),
                new BonusRate(10)
        );

        bonusHistory.addBonusRate(
                new MonthInterval(
                        YearMonth.of(2025, 12),
                        YearMonth.of(2025, 12)
                ),
                new BonusRate(15)
        );

        return new Employee(
                2, "Елена", "Попова", 7,
                salaryHistory, bonusHistory,workedDaysHistory
        );
    }
    private static Employee createEmployeeWithVacation() {
        SalaryHistory salaryHistory = new SalaryHistory();
        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2024, 4, 15),
                        LocalDate.of(2026, 12, 31)
                ),
                new SalaryRate(new Money(18000000))
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2024, 4, 15),
                        LocalDate.of(2025, 6, 20)
                ),
                WorkDayStatus.WORKED
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 6, 21),
                        LocalDate.of(2025, 7, 5)
                ),
                WorkDayStatus.VACATION
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 7, 6),
                        LocalDate.of(2026, 12, 31)
                ),
                WorkDayStatus.WORKED
        );

        return new Employee(
                3, "Иван", "Колесниченко", 5,
                salaryHistory, workedDaysHistory
        );
    }

    private static Employee createEmployeeWithNightAndSickLeave() {
        SalaryHistory salaryHistory = new SalaryHistory();
        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2024, 1, 22),
                        LocalDate.of(2026, 12, 31)
                ),
                new SalaryRate(new Money(20000000))
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2024, 1, 22),
                        LocalDate.of(2025, 10, 12)
                ),
                WorkDayStatus.WORKED
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 10, 13),
                        LocalDate.of(2025, 10, 24)
                ),
                WorkDayStatus.SICK_LEAVE
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 10, 25),
                        LocalDate.of(2026, 12, 31)
                ),
                WorkDayStatus.WORKED
        );

        nightShiftRateHistory.addNightRate(
                new DateInterval(
                        LocalDate.of(2024, 1, 22),
                        LocalDate.of(2026, 12, 31)
                ),
                new NightRate(new Money(250000))
        );

        workedNightsHistory.addWorkedNight(
                new DateInterval(
                        LocalDate.of(2025, 3, 10),
                        LocalDate.of(2025, 3, 14)
                )
        );

        workedNightsHistory.addWorkedNight(
                new DateInterval(
                        LocalDate.of(2026, 2, 16),
                        LocalDate.of(2026, 2, 20)
                )
        );

        return new Employee(
                4, "Владимир", "Петров", 12,
                salaryHistory,
                workedDaysHistory,
                nightShiftRateHistory,
                workedNightsHistory
        );
    }

    private static Employee createFullEmployee() {
        SalaryHistory salaryHistory = new SalaryHistory();
        WorkedDaysHistory workedDaysHistory = new WorkedDaysHistory();
        NightShiftRateHistory nightShiftRateHistory = new NightShiftRateHistory();
        WorkedNightsHistory workedNightsHistory = new WorkedNightsHistory();
        BonusHistory bonusHistory = new BonusHistory();
        EnforcementOrderPercent enforcementOrderPercent =
                new EnforcementOrderPercent(20);

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2024, 2, 5),
                        LocalDate.of(2025, 8, 31)
                ),
                new SalaryRate(new Money(25000000))
        );

        salaryHistory.addSalaryRate(
                new DateInterval(
                        LocalDate.of(2025, 9, 1),
                        LocalDate.of(2026, 12, 31)
                ),
                new SalaryRate(new Money(32000000))
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2024, 2, 5),
                        LocalDate.of(2025, 6, 9)
                ),
                WorkDayStatus.WORKED
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 6, 10),
                        LocalDate.of(2025, 6, 21)
                ),
                WorkDayStatus.VACATION
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2025, 6, 22),
                        LocalDate.of(2026, 3, 15)
                ),
                WorkDayStatus.WORKED
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2026, 3, 16),
                        LocalDate.of(2026, 3, 27)
                ),
                WorkDayStatus.SICK_LEAVE
        );

        workedDaysHistory.addWorkedDaysStatus(
                new DateInterval(
                        LocalDate.of(2026, 3, 28),
                        LocalDate.of(2026, 12, 31)
                ),
                WorkDayStatus.WORKED
        );

        nightShiftRateHistory.addNightRate(
                new DateInterval(
                        LocalDate.of(2024, 2, 5),
                        LocalDate.of(2026, 12, 31)
                ),
                new NightRate(new Money(300000))
        );

        workedNightsHistory.addWorkedNight(
                new DateInterval(
                        LocalDate.of(2025, 11, 10),
                        LocalDate.of(2025, 11, 14)
                )
        );

        workedNightsHistory.addWorkedNight(
                new DateInterval(
                        LocalDate.of(2026, 4, 6),
                        LocalDate.of(2026, 4, 10)
                )
        );

        bonusHistory.addBonusRate(
                new MonthInterval(
                        YearMonth.of(2024, 12),
                        YearMonth.of(2024, 12)
                ),
                new BonusRate(10)
        );

        bonusHistory.addBonusRate(
                new MonthInterval(
                        YearMonth.of(2025, 12),
                        YearMonth.of(2025, 12)
                ),
                new BonusRate(15)
        );

        bonusHistory.addBonusRate(
                new MonthInterval(
                        YearMonth.of(2026, 12),
                        YearMonth.of(2026, 12)
                ),
                new BonusRate(20)
        );

        return new Employee(
                5, "Александр", "Арепов", 12,
                salaryHistory,
                workedDaysHistory,
                nightShiftRateHistory,
                workedNightsHistory,
                bonusHistory,
                enforcementOrderPercent
        );
    }

}
