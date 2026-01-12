package jokes.jokes.controller;

import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("api/joke")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @GetMapping("/general")
    public List<Joke> getAllJokes() {
        return jokeService.getAllJokes(JokeCategory.GENERAL);
    }
}
