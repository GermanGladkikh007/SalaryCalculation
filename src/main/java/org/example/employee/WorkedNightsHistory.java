package org.example.employee;

import org.example.common.date_intervals.DateInterval;
import org.example.exception.OverlappingDateIntervalException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Хранение истории выхода в ночную смену
 * Отсутсвует геттеры, чтобы не было возможности извне менять список
 * Для этого есть отдельные методы, позволяющие обращаться к Map извне
 */
public class WorkedNightsHistory {

    private final List<DateInterval> workedNightsHistory;


    public WorkedNightsHistory() {
        workedNightsHistory = new ArrayList<>();
    }

    /**
     * Добавление интервалов с отработанными сменами
     * с обработкой исключения при пересечении интервалов.
     * Делаем для List, поэтому нужно находить индекс места, куда вставить
     * интервал в списке
     * Так он получается отсортированным автоматически
     * @param interval
     */

    public void addWorkedNight(DateInterval interval) {
        if (workedNightsHistory.isEmpty()) {
            workedNightsHistory.add(interval);
            return;
        }
        int index = 0;

        while (index < workedNightsHistory.size() &&
                workedNightsHistory.get(index).start().isBefore(interval.start())) {
            index++;
        }

        DateInterval previous = index > 0 ? workedNightsHistory.get(index - 1) : null;
        DateInterval next = index < workedNightsHistory.size() ? workedNightsHistory.get(index) : null;

        if (previous != null && previous.overlaps(interval) || next != null && next.overlaps(interval)) {
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        workedNightsHistory.add(index,interval);
    }

    /**
     * Защищенный проход по Map с условием
     * @param condition
     * @param action
     */

    public void forEachMatching(Predicate<DateInterval> condition, Consumer<DateInterval> action) {
        for(DateInterval interval : workedNightsHistory) {
            if(condition.test(interval)) {
                action.accept(interval);
            }
        }
    }


}
