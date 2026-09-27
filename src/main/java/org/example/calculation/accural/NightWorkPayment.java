package org.example.calculation.accural;

import org.example.calculation.Accrual;
import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.NightRate;
import org.example.employee.Employee;
import org.example.period.NightPayrollPeriods;
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

    private Employee employee;
    private PayrollPeriod payrollPeriod;
    private NightPayrollPeriods nightPayrollPeriods;
    private Money payrollPeriodNightPayment = Money.ZERO;

    public NightWorkPayment (Employee employee, PayrollPeriod payrollPeriod, NightPayrollPeriods nightPayrollPeriods) {
        this.employee = employee;
        this.payrollPeriod = payrollPeriod;
        this.nightPayrollPeriods = nightPayrollPeriods;
    }

    public Money getPayrollPeriodNightPayment () {return payrollPeriodNightPayment;}

    @Override
    public Money calculate() {
        List<NightWorkPaymentSegment> nightWorkPaymentSegments = getNightWorkPaymentSegments();
        Money nightPayment = Money.ZERO;
        for(NightWorkPaymentSegment segment : nightWorkPaymentSegments){
            Money segmentNightRateAmount = segment.nightRate().nightRate();
            long days = segment.workedNights().getDays();
            nightPayment = nightPayment.add(segmentNightRateAmount.multiply(days));
        }
        payrollPeriodNightPayment = nightPayment;
        return nightPayment;
    }

    @Override
    public String getType() {
        return "Night work payment";
    }

    @Override
    public String getAmount() {
        return Long.toString(payrollPeriodNightPayment.kopecks());
    }

    /**
     * Делим на сегменты ночных выплат, попадающих в расчетный период
     * Также делим, учитывая переход интервалов через пересечение месяцев
     * @return список сегментов, в котором каждому сегменту соответсвует интервал и ставка в ночную смену на нем
     */
    private List<NightWorkPaymentSegment> getNightWorkPaymentSegments() {
        List<NightWorkPaymentSegment> nightWorkPaymentSegments = new ArrayList<>();
        NavigableMap<DateInterval, NightRate> nightShiftRateHistoryMap = employee.nightShiftRateHistory().getNightRateHistory();

        for(var entry : nightShiftRateHistoryMap.entrySet()) {
            DateInterval historyInterval = entry.getKey();
            NightRate nightRate = entry.getValue();
            for(DateInterval workedNights : nightPayrollPeriods.getNightPayrollPeriods()){
                if(historyInterval.overlaps(workedNights) && workedNights.overlaps(payrollPeriod.payrollDataInterval())){
                    nightWorkPaymentSegments.add(newNightWorkPaymentSegment(payrollPeriod.payrollDataInterval(), historyInterval, workedNights, nightRate));
                }
            }

        }
        return splitByMonth(nightWorkPaymentSegments);
    }

    /**
     * Создание нового сегмента на основе
     * пересечения интервала отработанных ночных смен с расчетным периодом
     * и интервалами, в которых действует определенная ставка за ночную смену
     * @param payrollPeriod
     * @param historyInterval
     * @param workedNights
     * @param nightRate
     * @return
     */
    private NightWorkPaymentSegment newNightWorkPaymentSegment(DateInterval payrollPeriod, DateInterval historyInterval, DateInterval workedNights, NightRate nightRate) {
        LocalDate payrollStart = payrollPeriod.start().isAfter(workedNights.start()) ? payrollPeriod.start() : workedNights.start();
        LocalDate payrollEnd = payrollPeriod.end().isBefore(workedNights.end()) ? payrollPeriod.end() : workedNights.end();
        LocalDate start = historyInterval.start().isAfter(payrollStart) ? historyInterval.start() : payrollStart;
        LocalDate end = historyInterval.end().isBefore(payrollEnd) ? historyInterval.end() : payrollEnd;


        return new NightWorkPaymentSegment(new DateInterval(start,end),nightRate);
    }

    /**
     * Разделение уже получившихся сегментов по месяцам, чтобы
     * каждый сегмент был частью определенного месяца
     * @param nightWorkPaymentSegments
     * @return
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
