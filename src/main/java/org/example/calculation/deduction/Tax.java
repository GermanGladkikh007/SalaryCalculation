package org.example.calculation.deduction;

import org.example.calculation.Deduction;
import org.example.common.Money;
import org.example.common.rates.TaxRate;
import org.example.employee.Employee;
import org.example.employee.IncomeCalculator;
import org.example.payroll.PayrollPeriod;

import java.time.YearMonth;


/**
 * Класс для расчета НДФЛ
 * Основной принцип - взять сумму дохода с начала года
 * сопоставить с прогрессивной налоговой ставкой
 * Далее взять процент по частям от итогового дохода за расчетный период
 */
public class Tax implements Deduction {

    private final PayrollPeriod payrollPeriod;
    private final Employee employee;

    public Tax(Employee employee, PayrollPeriod payrollPeriod) {
        this.payrollPeriod = payrollPeriod;
        this.employee = employee;
    }

    /**
     * Расчет налога для отдельного месяца
     * Затем в calculate все суммируется
     * @param income
     * @param incomeThisMonth
     * @return
     */
    private Money calculateTax(Money income, Money incomeThisMonth) {
        long numerator; long denominator;
        Money incomeWithThisMonth = income.add(incomeThisMonth);
        Money tax = Money.ZERO;
        if(income.kopecks() <= 2_400_000_00) {

            if (income.kopecks() + incomeThisMonth.kopecks() <= 2_400_000_00) {
                numerator = TaxRate.UP_TO_2_4_MILLION.getNumerator();
                denominator = TaxRate.UP_TO_2_4_MILLION.getDenominator();
                tax = tax.add(incomeThisMonth).multiply(numerator).divide(denominator);
            } else if (income.kopecks() + incomeThisMonth.kopecks() <= 5_000_000_00) {
                Money border = new Money(2_400_000_00);
                numerator = TaxRate.UP_TO_2_4_MILLION.getNumerator();
                denominator = TaxRate.UP_TO_2_4_MILLION.getDenominator();
                tax = tax.add(border.subtract(income).multiply(numerator).divide(denominator));
                numerator = TaxRate.FROM_2_4_TO_5_MILLION.getNumerator();
                denominator = TaxRate.FROM_2_4_TO_5_MILLION.getDenominator();
                tax = tax.add(incomeWithThisMonth.subtract(border).multiply(numerator).divide(denominator));
            }
        }else if(income.kopecks() <= 5_000_000_00){
            if (income.kopecks() + incomeThisMonth.kopecks() <= 5_000_000_00) {
                numerator = TaxRate.FROM_2_4_TO_5_MILLION.getNumerator();
                denominator = TaxRate.FROM_2_4_TO_5_MILLION.getDenominator();

                tax = tax.add(incomeThisMonth)
                        .multiply(numerator)
                        .divide(denominator);

            } else if (income.kopecks() + incomeThisMonth.kopecks() <= 20_000_000_00) {
                Money border = new Money(5_000_000_00);

                numerator = TaxRate.FROM_2_4_TO_5_MILLION.getNumerator();
                denominator = TaxRate.FROM_2_4_TO_5_MILLION.getDenominator();

                tax = tax.add(border.subtract(income))
                        .multiply(numerator)
                        .divide(denominator);

                numerator = TaxRate.FROM_5_TO_20_MILLION.getNumerator();
                denominator = TaxRate.FROM_5_TO_20_MILLION.getDenominator();

                tax = tax.add(incomeWithThisMonth.subtract(border).multiply(numerator).divide(denominator));

            }
        }else if(income.kopecks() <= 20_000_000_00){
            if (income.kopecks() + incomeThisMonth.kopecks() <= 20_000_000_00) {
                numerator = TaxRate.FROM_5_TO_20_MILLION.getNumerator();
                denominator = TaxRate.FROM_5_TO_20_MILLION.getDenominator();

                tax = tax.add(incomeThisMonth)
                        .multiply(numerator)
                        .divide(denominator);

            } else if (income.kopecks() + incomeThisMonth.kopecks() <= 50_000_000_00L) {
                Money border = new Money(20_000_000_00);

                numerator = TaxRate.FROM_5_TO_20_MILLION.getNumerator();
                denominator = TaxRate.FROM_5_TO_20_MILLION.getDenominator();

                tax = tax.add(border.subtract(income))
                        .multiply(numerator)
                        .divide(denominator);

                numerator = TaxRate.FROM_20_TO_50_MILLION.getNumerator();
                denominator = TaxRate.FROM_20_TO_50_MILLION.getDenominator();

                tax = tax.add(incomeWithThisMonth.subtract(border).multiply(numerator).divide(denominator));

            }
        }else if(income.kopecks() <= 50_000_000_00L){
            if (income.kopecks() + incomeThisMonth.kopecks() <= 50_000_000_00L) {
                numerator = TaxRate.FROM_20_TO_50_MILLION.getNumerator();
                denominator = TaxRate.FROM_20_TO_50_MILLION.getDenominator();

                tax = tax.add(incomeThisMonth)
                        .multiply(numerator)
                        .divide(denominator);

            } else {
                Money border = new Money(50_000_000_00L);

                numerator = TaxRate.FROM_20_TO_50_MILLION.getNumerator();
                denominator = TaxRate.FROM_20_TO_50_MILLION.getDenominator();

                tax = tax.add(border.subtract(income))
                        .multiply(numerator)
                        .divide(denominator);

                numerator = TaxRate.OVER_50_MILLION.getNumerator();
                denominator = TaxRate.OVER_50_MILLION.getDenominator();

                tax = tax.add(incomeWithThisMonth.subtract(border).multiply(numerator).divide(denominator));

            }
        }else{
            numerator = TaxRate.OVER_50_MILLION.getNumerator();
            denominator = TaxRate.OVER_50_MILLION.getDenominator();

            tax = tax.add(incomeThisMonth)
                    .multiply(numerator)
                    .divide(denominator);
        }

        return tax;
    }

    @Override
    public Money calculate() {
        Money income = Money.ZERO;
        Money incomeThisMonth = Money.ZERO;
        Money tax = Money.ZERO;
        IncomeCalculator incomeCalculator = new IncomeCalculator(employee);
        YearMonth yearMonth = payrollPeriod.payrollMonthInterval().start();
        while(!yearMonth.isAfter(payrollPeriod.payrollMonthInterval().end())){
            income = incomeCalculator.getIncomeBefore(yearMonth);
            incomeThisMonth = incomeCalculator.getIncomeMonth(yearMonth);
            tax = tax.add(calculateTax(income, incomeThisMonth));
            yearMonth = yearMonth.plusMonths(1);
        }

        return tax;
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public String getAmount() {
        return "";
    }
}
