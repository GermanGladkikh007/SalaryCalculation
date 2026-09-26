package org.example.calculation;


import org.example.common.Money;

public interface CalculationElement {

    public Money calculate();

    public String getType();

    public String getAmount();

}
