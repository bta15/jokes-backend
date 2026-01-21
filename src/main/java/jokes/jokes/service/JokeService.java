package jokes.jokes.service;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.JokeEntity;
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
        List<JokeEntity> jokeEntities = jokeRepository.findAll();
        List<CsvJoke> csvJokes = new ArrayList<>();
        jokeEntities.forEach(joke -> {
            CsvJoke csvJoke = new CsvJoke();
            csvJoke.setJoke(joke.getWitz());
            csvJoke.setCategory(joke.getKategorie().name().toLowerCase());
            csvJokes.add(csvJoke);
        });
        return csvJokes;
    }

    public JokeDto update(Long id, JokeDto jokeDto) {
        var stored = jokeRepository.findById(id);
        if (stored.isPresent()) {
            JokeEntity jokeEntity = stored.get();
            jokeEntity.setKategorie(jokeDto.kategorie());
            jokeEntity.setWitz(jokeDto.witz());
            var updated = jokeRepository.save(jokeEntity);
            return new JokeDto(updated.getWitz(), updated.getKategorie());
        } else {
            System.out.println("Joke not found");
            return null;
        }
    }

    public void delete(Long id) {
        jokeRepository.deleteById(id);
    }
}
