import org.example.employee.*;

public class EmployeeCreator {

    public Employee createEmployee(SalaryHistory salaryHistory,
                                    NightShiftRateHistory nightShiftRateHistory,
                                    WorkedDaysHistory workedDaysHistory,
                                    WorkedNightsHistory workedNightsHistory) {
        return new Employee(15, "Киллиан", "Мбаппе",
                salaryHistory, workedDaysHistory, nightShiftRateHistory, workedNightsHistory);
    }

}
