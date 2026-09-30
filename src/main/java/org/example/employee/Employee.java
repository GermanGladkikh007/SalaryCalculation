package org.example.employee;

import org.example.common.*;
import org.example.common.date_intervals.DateInterval;
import org.example.common.date_intervals.MonthInterval;
import org.example.common.rates.BonusRate;
import org.example.common.rates.NightRate;
import org.example.common.rates.SalaryRate;

/**
 * Класс для хранения информации о сотруднике
 * и его работе
 * @param employeeId
 * @param employeeName
 * @param employeeSurname
 * @param salaryHistory
 * @param workedDaysHistory
 * @param nightShiftRateHistory
 * @param workedNightsHistory
 */
public record Employee(
        int employeeId,
        String employeeName,
        String employeeSurname,
        long insuranceExperience,
        SalaryHistory salaryHistory,
        WorkedDaysHistory workedDaysHistory,
        NightShiftRateHistory nightShiftRateHistory,
        WorkedNightsHistory workedNightsHistory,
        BonusHistory bonusHistory) {


    public String getFullName() {
        return employeeName + " " + employeeSurname;
    }

    public Employee(int employeeId,
                    String employeeName,
                    String employeeSurname,
                    long insuranceExperience){
        this(
                employeeId,
                employeeName,
                employeeSurname,
                insuranceExperience,
                new SalaryHistory(),
                new WorkedDaysHistory(),
                new NightShiftRateHistory(),
                new WorkedNightsHistory(),
                new BonusHistory()
        );
    }

    public Employee(int employeeId,
                    String employeeName,
                    String employeeSurname,
                    SalaryHistory salaryHistory,
                    BonusHistory bonusHistory){
        this(
                employeeId,
                employeeName,
                employeeSurname,
                0,
                salaryHistory,
                new WorkedDaysHistory(),
                new NightShiftRateHistory(),
                new WorkedNightsHistory(),
                bonusHistory
        );
    }

    public Employee(int employeeId,
                    String employeeName,
                    String employeeSurname,
                    long insuranceExperience,
                    SalaryHistory salaryHistory,
                    WorkedDaysHistory workedDaysHistory){
        this(
                employeeId,
                employeeName,
                employeeSurname,
                insuranceExperience,
                salaryHistory,
                workedDaysHistory,
                new NightShiftRateHistory(),
                new WorkedNightsHistory(),
                new BonusHistory()
        );
    }

    public void addSalaryRate(DateInterval dateInterval, SalaryRate salaryRate) {
        salaryHistory.addSalaryRate(dateInterval, salaryRate);
    }

    public void addWorkedDaysStatus(DateInterval dateInterval, WorkDayStatus workDayStatus) {
        workedDaysHistory.addWorkedDaysStatus(dateInterval, workDayStatus);
    }

    public void addNightRate(DateInterval dateInterval, NightRate nightRate) {
        nightShiftRateHistory.addNightRate(dateInterval, nightRate);
    }

    public void addWorkedNight(DateInterval dateInterval) {
        workedNightsHistory.addWorkedNight(dateInterval);
    }

    public void addBonusRate(MonthInterval monthInterval, BonusRate bonusRate) {
        bonusHistory.addBonusRate(monthInterval, bonusRate);
    }
}
