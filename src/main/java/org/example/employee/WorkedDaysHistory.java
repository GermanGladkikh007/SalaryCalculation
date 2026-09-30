package org.example.employee;

import org.example.common.date_intervals.DateInterval;
import org.example.common.WorkDayStatus;
import org.example.exception.OverlappingDateIntervalException;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Хранение истории выходов/невыходов на работу
 * Отсутсвует геттеры, чтобы не было возможности извне менять список
 * Для этого есть отдельные методы, позволяющие обращаться к Map извне
 */

public class WorkedDaysHistory {
    private final NavigableMap<DateInterval, WorkDayStatus> workedDaysHistory;

    public WorkedDaysHistory() {
        workedDaysHistory = new TreeMap<>();
    }

    /**
     * Добавление нового интервала рабочего статуса
     * Обязательно проверяем здесь пересечение интервалов
     * @param interval
     * @param workDayStatus
     */
    public void addWorkedDaysStatus(DateInterval interval, WorkDayStatus workDayStatus) {
        if (workedDaysHistory.isEmpty()) {
            workedDaysHistory.put(interval, workDayStatus);
            return;
        }

        DateInterval previous = workedDaysHistory.floorKey(interval);
        DateInterval next = workedDaysHistory.ceilingKey(interval);

        if (previous != null && previous.overlaps(interval) || next != null && next.overlaps(interval)) {
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        workedDaysHistory.put(interval, workDayStatus);
    }

    /**
     * Защищенный проход по Map с условием
     * для Salary
     * @param condition
     * @param action
     */

    public void salaryForEachMatching(Predicate<DateInterval> condition, Consumer<DateInterval> action) {
        for (var entry : workedDaysHistory.entrySet()) {
            DateInterval dateInterval = entry.getKey();
            WorkDayStatus workDayStatus = entry.getValue();
            if (condition.test(dateInterval) && workDayStatus == WorkDayStatus.WORKED) {
                action.accept(dateInterval);
            }
        }
    }

    /**
     * Защищенный проход по Map с условием
     * для VacationPay
     * @param condition
     * @param action
     */
    public void vacationForEachMatching(Predicate<DateInterval> condition, Consumer<DateInterval> action) {
        for (var entry : workedDaysHistory.entrySet()) {
            DateInterval dateInterval = entry.getKey();
            WorkDayStatus workDayStatus = entry.getValue();
            if (condition.test(dateInterval) && workDayStatus == WorkDayStatus.VACATION) {
                action.accept(dateInterval);
            }
        }
    }

    /**
     * Защищенный проход по Map с условием
     * для SickLeave
     * @param condition
     * @param action
     */
    public void sickLeaveForEachMatching(Predicate<DateInterval> condition, Consumer<DateInterval> action) {
        for (var entry : workedDaysHistory.entrySet()) {
            DateInterval dateInterval = entry.getKey();
            WorkDayStatus workDayStatus = entry.getValue();
            if (condition.test(dateInterval) && workDayStatus == WorkDayStatus.SICK_LEAVE) {
                action.accept(dateInterval);
            }
        }
    }

}
