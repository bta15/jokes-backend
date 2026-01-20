package jokes.jokes.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.service.csv.CsvJoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JokeImportService {

    @Autowired
    private JokeRepository jokeRepository;

    public void importJokes(InputStreamSource file) throws IOException {
        CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
        CsvSchema schema = mapper.typedSchemaFor(CsvJoke.class).withHeader().withColumnSeparator(';');

        List<CsvJoke> csvJokes = mapper.readerFor(CsvJoke.class)
                .with(schema)
                .<CsvJoke>readValues(file.getInputStream())
                .readAll();

        csvJokes.forEach(System.out::println);

        List<Joke> newJokeList = new ArrayList<>();
        csvJokes.forEach(newJoke -> {
            if (newJoke.getJoke() != null && !newJoke.getJoke().isEmpty() && jokeRepository.existsByText(newJoke.getJoke())) {
                return;
            }
            JokeCategory newCategory = JokeCategory.valueOf(newJoke.getCategory().toUpperCase());
            Joke joke = new Joke();
            joke.setCategory(newCategory);
            joke.setText(newJoke.getJoke());
            newJokeList.add(joke);
        });
        jokeRepository.saveAll(newJokeList);
    }
}
