package jokes.jokes.service;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.JokeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}
