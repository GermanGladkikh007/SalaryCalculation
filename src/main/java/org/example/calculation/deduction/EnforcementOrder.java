package org.example.calculation.deduction;

import org.example.calculation.Deduction;
import org.example.common.Money;
import org.example.employee.Employee;

public class EnforcementOrder implements Deduction {

    private final Employee employee;
    private final Money totalAmountWithoutEnforcementOrder;
    private Money totalAmount;

    public EnforcementOrder(Employee employee, Money totalAmountWithoutEnforcementOrder) {
        this.employee = employee;
        this.totalAmountWithoutEnforcementOrder = totalAmountWithoutEnforcementOrder;
    }

    @Override
    public Money calculate() {
        long percent = employee.enforcementOrderPercent().percent();
        totalAmount =totalAmountWithoutEnforcementOrder.multiply(percent).divide(100);
        return totalAmount;
    }

    @Override
    public String getType() {
        return "Исполнительный лист";
    }

    @Override
    public String getAmount() {
        return Long.toString(totalAmount.kopecks());
    }
}
