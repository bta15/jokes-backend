package jokes.jokes.util;

import jokes.jokes.entity.JokeCategory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static jokes.jokes.util.DateUtil.isBetween;

public class JokeCategoryUtil {

    private static final Map<JokeCategory, DateRange> categoryDates = new HashMap<>();

    private static final LocalDate today = LocalDate.now();

    private static final LocalDate christmasStart = LocalDate.of(today.getYear(), 12, 1);
    private static final LocalDate christmasEnd = LocalDate.of(today.getYear(), 12, 26);


    static {
        categoryDates.put(JokeCategory.WEIHNACHTEN, new DateRange(christmasStart, christmasEnd));
        //TODO check/add more categories here
    }

    public static JokeCategory getCategory() {
        for (JokeCategory jokeCategory : categoryDates.keySet()) {
            if (isBetween(today, categoryDates.get(jokeCategory))) {
                return jokeCategory;
            }
        }
        return JokeCategory.ALLGEMEIN;
    }
}
