package org.example.payroll;

import org.example.calculation.Accrual;
import org.example.calculation.Deduction;
import org.example.calculation.accural.*;
import org.example.calculation.deduction.EnforcementOrder;
import org.example.calculation.deduction.Tax;
import org.example.common.Money;
import org.example.employee.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс расчетного листа
 * Методы получают данные и выводят их
 */
public class Payslip {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;

    public Payslip(Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    private List<Accrual> getAccruals() {
        List<Accrual> accruals = new ArrayList<>();

        Salary salary = new Salary(employee, payrollPeriod);
        Bonus bonus = new Bonus(employee, payrollPeriod);
        NightWorkPayment nightWorkPayment = new NightWorkPayment(employee, payrollPeriod);
        SickLeavePay sickLeavePay = new SickLeavePay(employee, payrollPeriod);
        VacationPay vacationPay = new VacationPay(employee, payrollPeriod);

        accruals.add(salary);
        accruals.add(bonus);
        accruals.add(nightWorkPayment);
        accruals.add(vacationPay);

        return accruals;
    }

    private List<Deduction> getDeductions() {
        List<Deduction> deductions = new ArrayList<>();

        Tax tax = new Tax(employee, payrollPeriod);
        Money taxAmount = tax.calculate();
        EnforcementOrder enforcementOrder = new EnforcementOrder(employee,taxAmount);

        deductions.add(tax);
        deductions.add(enforcementOrder);
        return deductions;
    }

    private String kopecksToRubles(Money money) {
        String rubles = Long.toString(money.kopecks() / 100) + ',' + Long.toString(money.kopecks() % 100) + 'р';
        return rubles;
    }



    public void printPayslip(){
        List<Accrual> accruals = getAccruals();
        List<Deduction> deductions = getDeductions();

        System.out.println("========================================");
        System.out.println("          РАСЧЁТНЫЙ ЛИСТ                ");
        System.out.println("========================================");
        System.out.println();

        System.out.printf("Сотрудник: %s%n",employee.getFullName());
        System.out.printf("Табельный номер: %d%n", employee.employeeId());
        System.out.printf("Расчетный период: %s%n", payrollPeriod.toString());
        System.out.println();

        System.out.println("НАЧИСЛЕНИЯ");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-35s %15s%n", "Вид начисления", "Сумма");
        System.out.println("------------------------------------------------------------");

        Money totalAccruals = Money.ZERO;

        for(Accrual accrual: accruals){
            Money accrualAmount = accrual.calculate();
            System.out.printf("%-35s %15s%n", accrual.getType(), kopecksToRubles(accrualAmount));
            totalAccruals = totalAccruals.add(accrualAmount);
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("%-35s %15s%n", "ИТОГО НАЧИСЛЕНО", kopecksToRubles(totalAccruals));

        System.out.println();
        System.out.println("УДЕРЖАНИЯ");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-35s %15s%n", "Вид удержания", "Сумма");
        System.out.println("------------------------------------------------------------");

        Money totalDeductions = Money.ZERO;
        for(Deduction deduction: deductions){
            Money deductionAmount = deduction.calculate();
            System.out.printf("%-35s %15s%n", deduction.getType(), kopecksToRubles(deductionAmount));
            totalDeductions = totalDeductions.add(deductionAmount);
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf("%-35s %15s%n", "ИТОГО УДЕРЖАНО", kopecksToRubles(totalDeductions));

        System.out.println();
        System.out.printf("%-35s %15s%n", "К ВЫПЛАТЕ", kopecksToRubles(totalAccruals.subtract(totalDeductions)));

        System.out.println("============================================================");

    }
}
