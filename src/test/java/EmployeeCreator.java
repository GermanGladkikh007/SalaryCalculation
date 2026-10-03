import org.example.employee.*;

public class EmployeeCreator {

    public Employee createEmployee(SalaryHistory salaryHistory,
                                    NightShiftRateHistory nightShiftRateHistory,
                                    WorkedDaysHistory workedDaysHistory,
                                    WorkedNightsHistory workedNightsHistory,
                                   BonusHistory bonusHistory) {
        return new Employee(15, "Киллиан", "Мбаппе",0,
                salaryHistory, workedDaysHistory, nightShiftRateHistory, workedNightsHistory,bonusHistory,new EnforcementOrderPercent(15));
    }

    public Employee createEmployee(SalaryHistory salaryHistory,
                                   BonusHistory bonusHistory) {
        return new Employee(15, "Киллиан", "Мбаппе",15,
                salaryHistory, bonusHistory);
    }

    public Employee createEmployee(long insuranceExperience, SalaryHistory salaryHistory, WorkedDaysHistory workedDaysHistory) {
        return new Employee(15, "Киллиан", "Мбаппе",insuranceExperience,salaryHistory, workedDaysHistory, new EnforcementOrderPercent(15));
    }

    public Employee createEmployee(long insuranceExperience,
                                   SalaryHistory salaryHistory,
                                   NightShiftRateHistory nightShiftRateHistory,
                                   WorkedDaysHistory workedDaysHistory,
                                   WorkedNightsHistory workedNightsHistory,
                                   BonusHistory bonusHistory) {
        return new Employee(15, "Киллиан", "Мбаппе",insuranceExperience,
                salaryHistory, workedDaysHistory, nightShiftRateHistory, workedNightsHistory,bonusHistory, new EnforcementOrderPercent(15));
    }

}
