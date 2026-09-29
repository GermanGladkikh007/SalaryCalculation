package org.example.employee;

import org.example.common.DateInterval;
import org.example.common.NightRate;
import org.example.common.SalaryRate;
import org.example.common.WorkDayStatus;

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
        SalaryHistory salaryHistory,
        WorkedDaysHistory workedDaysHistory,
        NightShiftRateHistory nightShiftRateHistory,
        WorkedNightsHistory workedNightsHistory) {


    public String getFullName() {
        return employeeName + " " + employeeSurname;
    }

    /**
     * Конструктор для нового сотрудника
     * @param employeeId
     * @param employeeName
     * @param employeeSurname
     */
    public Employee(int employeeId,
                    String employeeName,
                    String employeeSurname){
        this(
                employeeId,
                employeeName,
                employeeSurname,
                new SalaryHistory(),
                new WorkedDaysHistory(),
                new NightShiftRateHistory(),
                new WorkedNightsHistory()
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

}
