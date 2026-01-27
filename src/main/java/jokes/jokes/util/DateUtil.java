package jokes.jokes.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static boolean isBetween(LocalDate date, DateRange dateRange) {
        return !date.isBefore(dateRange.start()) && !date.isAfter(dateRange.end());
    }

}
