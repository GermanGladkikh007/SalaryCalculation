package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.NightRate;
import org.example.employee.Employee;
import org.example.employee.NightShiftRateHistory;
import org.example.employee.WorkedNightsHistory;
import org.example.period.NightWorkPaymentSegment;
import org.example.period.PayrollPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;


/**
 *  Начисления за ночные смены сотруднику
 *  за расчетный период.
 *  Сопоставление интервалов дат производится
 *  по аналогии с сопоставлением в классе Salary
 */
public class NightWorkPayment implements Accrual {

    private final Employee employee;
    private final PayrollPeriod payrollPeriod;
    private Money payrollPeriodNightPayment = Money.ZERO;

    public NightWorkPayment (Employee employee, PayrollPeriod payrollPeriod) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
    }

    public Money getPayrollPeriodNightPayment () {return payrollPeriodNightPayment;}

    @Override
    public Money calculate() {
        List<NightWorkPaymentSegment> splittedByMonthNightWorkPaymentSegments = splitByMonth(getNightWorkPaymentSegments());

        Money nightPayment = Money.ZERO;
        for(NightWorkPaymentSegment segment : splittedByMonthNightWorkPaymentSegments){
            Money segmentNightRateAmount = segment.nightRate().nightRate();
            long days = segment.workedNights().getDays();
            nightPayment = nightPayment.add(segmentNightRateAmount.multiply(days));
        }
        payrollPeriodNightPayment = nightPayment;
        return nightPayment;
    }

    @Override
    public String getType() {
        return "Доплата за ночные смены";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodNightPayment.kopecks());
    }

    /**
     * Делим на сегменты ночных выплат, попадающих и в расчетный период и в интервалы выходов на ночные смены
     * @return список сегментов, в котором каждому сегменту соответсвует интервал и ставка в ночную смену на нем
     */
    private List<NightWorkPaymentSegment> getNightWorkPaymentSegments() {
        List<NightWorkPaymentSegment> nightWorkPaymentSegments = new ArrayList<>();

        NightShiftRateHistory nightShiftRateHistory = employee.nightShiftRateHistory();
        WorkedNightsHistory workedNightsHistory = employee.workedNightsHistory();
        DateInterval payrollDateInterval = payrollPeriod.payrollMonthInterval().toDateInterval();

        nightShiftRateHistory.forEachMatching(
                nightInterval -> nightInterval.overlaps(payrollDateInterval),
                (nightInterval, nightRate) -> {
                    DateInterval actualNightInterval = nightInterval.intersection(payrollDateInterval);

                    workedNightsHistory.forEachMatching(workedNightInterval -> workedNightInterval.overlaps(actualNightInterval),
                            workedNightInterval -> nightWorkPaymentSegments.add(
                                    new NightWorkPaymentSegment(
                                            actualNightInterval.intersection(workedNightInterval), nightRate)
                            )
                    );
                }
        );

        return nightWorkPaymentSegments;
    }

    /**
     * Разделение уже получившихся сегментов по месяцам, чтобы
     * каждый сегмент был частью определенного месяца
     * @param nightWorkPaymentSegments
     * @return список сегментов, в котором каждому сегменту соответсвует интервал
     * в границе ровно одного месяца и ставка в ночную смену на этом интервале
     */
    private List<NightWorkPaymentSegment> splitByMonth(List<NightWorkPaymentSegment> nightWorkPaymentSegments) {
        List<NightWorkPaymentSegment> result = new ArrayList<>();

        for(NightWorkPaymentSegment segment : nightWorkPaymentSegments){
            LocalDate start = segment.workedNights().start();
            LocalDate end = segment.workedNights().end();

            LocalDate currentStart = start;

            while(!currentStart.isAfter(end)){
                LocalDate endOfMonth = currentStart.withDayOfMonth(currentStart.lengthOfMonth());

                LocalDate currentEnd = endOfMonth.isBefore(end) ? endOfMonth : end;

                DateInterval dateInterval = new DateInterval(currentStart,currentEnd);

                result.add(new NightWorkPaymentSegment(dateInterval, segment.nightRate()));

                currentStart = currentEnd.plusDays(1);
            }

        }

        return result;
    }


}
