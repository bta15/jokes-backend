package jokes.jokes.service;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.database.entity.JokeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Log
@Service
@RequiredArgsConstructor
public class JokeService {

    private final JokeRepository jokeRepository;

    public JokeEntity getJoke(Long id) {
        var joke = jokeRepository.findById(id);
        if (joke.isPresent()) {
            return joke.get();
        } else {
            throw new JokeNotFoundException("Joke with id " + id + " not found");
        }
    }

    public List<JokeEntity> getAllJokes() {
        return jokeRepository.findAll();
    }

    public JokeEntity create(JokeDto jokeDto) {
        JokeEntity jokeEntity = new JokeEntity();
        jokeEntity.setWitz(jokeDto.witz());
        jokeEntity.setKategorie(jokeDto.kategorie());
        return jokeRepository.save(jokeEntity);
    }

    public JokeEntity update(Long id, JokeDto jokeDto) {
        var stored = jokeRepository.findById(id);
        if (stored.isPresent()) {
            JokeEntity jokeEntity = stored.get();
            jokeEntity.setKategorie(jokeDto.kategorie());
            jokeEntity.setWitz(jokeDto.witz());
            return jokeRepository.save(jokeEntity);
        } else {
            throw new JokeNotFoundException("Joke with id " + id + " not found");
        }
    }

    public void delete(Long id) {
        jokeRepository.deleteById(id);
    }

    public JokeEntity like(Long id) {
        var joke = getJoke(id);
        joke.setLikes(joke.getLikes() == null ? 1 : joke.getLikes() + 1);
        return jokeRepository.save(joke);
    }

    public List<JokeEntity> getTop() {
        return jokeRepository.findTop5ByOrderByLikesDesc();
    }

    public JokeEntity getRandomJokeByCategory(JokeCategory category) {
        var jokes = jokeRepository.findAllByKategorie(category);
        if (jokes.isEmpty()) {
            throw new JokeNotFoundException("No jokes found for category " + category);
        }
        return getRandomJoke(jokes);
    }

    public JokeEntity getJokeOfTheDay() {
        var category = getCategoryOfTheDay();
        var jokes = jokeRepository.findAllByKategorie(category);
        if (jokes.isEmpty()) {
            throw new JokeNotFoundException("No jokes found for category " + category);
        }

        return getRandomJoke(jokes);
    }

    private JokeEntity getRandomJoke(List<JokeEntity> jokes) {
        var random = new Random();
        var randomIndex =  random.nextInt(jokes.size());
        return jokes.get(randomIndex);
    }

    private JokeCategory getCategoryOfTheDay() {
        var today = LocalDate.now();
        var christmasStart = LocalDate.of(today.getYear(), 12, 1);
        var christmasEnd = LocalDate.of(today.getYear(), 12, 26);
        if (isBetween(today, christmasStart, christmasEnd)) {
            return JokeCategory.WEIHNACHTEN;
        } else {
            log.warning("No joke category found for today. Returning general category.");
            return JokeCategory.ALLGEMEIN;
        }
        //TODO check/add more categories here
    }

    private boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
