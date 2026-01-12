package jokes.jokes.service;

import jdk.jfr.Category;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokeService {

    @Autowired
    private JokeRepository jokeRepository;

    public List<Joke> getAllJokes(JokeCategory category) {
        return jokeRepository.findAllByCategory(category);
    }
}
