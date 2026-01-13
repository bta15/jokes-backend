package jokes.jokes.service.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvJoke {

    @JsonProperty("text")
    private String joke;

    @JsonProperty("kategorie")
    private String category;

    public String toString() {
        return "Joke: " + joke + " Category: " + category;
    }
}
