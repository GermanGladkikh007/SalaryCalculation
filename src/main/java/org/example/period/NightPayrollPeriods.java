package org.example.period;

import org.example.common.DateInterval;
import org.example.exception.NonContinuousDateIntervalException;
import org.example.exception.OverlappingDateIntervalException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Класс для хранения интервалов дат
 * с отработанными ночными сменами
 */
public class NightPayrollPeriods {

    private final List<DateInterval> nightPayrollPeriods;


    public NightPayrollPeriods() {
        nightPayrollPeriods = new ArrayList<>();
    }

    /**
     * По аналогии с SalaryHistory добавление интервалов
     * с обработкой исключения при пересечении интервалов.
     * Делаем для List, поэтому нужно находить индекс места, куда вставить
     * интервал в списке
     * Так он получается отсортированным автоматически
     * @param nightPayrollPeriod
     */
    public void addNightPayrollPeriod(DateInterval nightPayrollPeriod) {
        if (nightPayrollPeriods.isEmpty()) {
            nightPayrollPeriods.add(nightPayrollPeriod);
            return;
        }
        int index = 0;

        while (index < nightPayrollPeriods.size() &&
                nightPayrollPeriods.get(index).start().isBefore(nightPayrollPeriod.start())) {
            index++;
        }

        DateInterval previous = index > 0 ? nightPayrollPeriods.get(index - 1) : null;
        DateInterval next = index < nightPayrollPeriods.size() ? nightPayrollPeriods.get(index) : null;

        if (previous != null && previous.overlaps(nightPayrollPeriod) || next != null && next.overlaps(nightPayrollPeriod)) {
            throw new OverlappingDateIntervalException("Новый интервал дат имеет пересечения с другими интервалами");
        }

        nightPayrollPeriods.add(index,nightPayrollPeriod);
    }

    public List<DateInterval> getNightPayrollPeriods() {
        return nightPayrollPeriods;
    }
}
