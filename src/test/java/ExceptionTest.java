import org.example.common.DateInterval;
import org.example.common.Money;
import org.example.common.SalaryRate;
import org.example.employee.SalaryHistory;
import org.example.exception.OverlappingDateIntervalException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest {

    @Test
    void shouldThrowExceptionWhenSalaryIntervalsOverlap() {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.addSalaryRate(new DateInterval(LocalDate.of(2001, 1, 1), LocalDate.of(2001, 1, 20)),
                new SalaryRate(new Money(10000000)));
    }

    @Test
    void shouldThrowExceptionWhenDateIntervalsContinuous(){

    }
}
