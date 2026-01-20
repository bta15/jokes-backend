package jokes.jokes.service.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(value = {CsvHeaders.JOKE, CsvHeaders.CATEGORY})
public class CsvJoke {

    @JsonProperty(CsvHeaders.JOKE)
    private String joke;

    @JsonProperty(CsvHeaders.CATEGORY)
    private String category;

    public String toString() {
        return "Joke: " + joke + " Category: " + category;
    }
}
