package org.example.employee;

import org.example.common.DateInterval;
import org.example.common.SalaryRate;

public record Employee(
        int employeeId,
        String employeeName,
        String employeeSurname,
        SalaryHistory salaryHistory) {


    public String getFullName() {
        return employeeName + " " + employeeSurname;
    }

    /**
     * Добавление интервала дат с новой ставкой оклада
     * @param dateInterval
     * @param salaryRate
     */
    public void addSalaryRate(DateInterval dateInterval, SalaryRate salaryRate) {
        salaryHistory.addSalaryRate(dateInterval, salaryRate);
    }

}
