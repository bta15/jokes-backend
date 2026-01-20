package jokes.jokes.service;

import jdk.jfr.Category;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.service.csv.CsvJoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JokeService {

    @Autowired
    private JokeRepository jokeRepository;

    public List<CsvJoke> getAllJokes() {
        List<Joke> jokes = jokeRepository.findAll();
        List<CsvJoke> csvJokes = new ArrayList<>();
        jokes.forEach(joke -> {
            CsvJoke csvJoke = new CsvJoke();
            csvJoke.setJoke(joke.getText());
            csvJoke.setCategory(joke.getCategory().name().toLowerCase());
            csvJokes.add(csvJoke);
        });
        return csvJokes;
    }
}
