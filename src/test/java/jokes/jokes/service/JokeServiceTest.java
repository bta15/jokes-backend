package jokes.jokes.service;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.entity.JokeCategory;
import jokes.jokes.entity.JokeEntity;
import jokes.jokes.repository.JokeRepository;
import jokes.jokes.service.exception.JokeAlreadyExistsException;
import jokes.jokes.service.exception.JokeNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeServiceTest {


    @InjectMocks
    private JokeService jokeService;

    @Mock
    private JokeRepository repository;

    private static final Long ID = 1L;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getJoke_successful() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new JokeEntity()));

        var joke = jokeService.getJoke(ID);

        assertNotNull(joke);
    }

    @Test
    void getJoke_exception() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(JokeNotFoundException.class, () -> {
            jokeService.getJoke(ID);
        });

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllJokes() {
        var allJokes = Collections.singletonList(new JokeEntity());
        when(repository.findAll()).thenReturn(allJokes);

        var jokeList = jokeService.getAllJokes();
        assertNotNull(jokeList);

        verify(repository, times(1)).findAll();
    }

    @Test
    void createJoke_successful() {
        when(repository.save(any())).thenReturn(new JokeEntity());
        when(repository.existsByWitz(anyString())).thenReturn(false);

        var created = jokeService.create(new JokeDto("joke", JokeCategory.JOB));

        assertNotNull(created);
        verify(repository, times(1)).save(any());
    }

    @Test
    void createJoke_exception() {
        when(repository.existsByWitz(anyString())).thenReturn(true);

        assertThrows(JokeAlreadyExistsException.class, () -> {
            jokeService.create(new JokeDto("joke", JokeCategory.JOB));
        });

        verify(repository, times(1)).existsByWitz(anyString());
    }

    @Test
    void updateJoke_successful() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new JokeEntity()));
        when(repository.save(any())).thenReturn(new JokeEntity());

        var upated = jokeService.update(ID, new JokeDto("joke", JokeCategory.JOB));

        assertNotNull(upated);
        verify(repository, times(1)).save(any());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void updateJoke_exception() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(JokeNotFoundException.class, () -> {
            jokeService.update(ID, new JokeDto("joke", JokeCategory.JOB));
        });

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void deleteJoke_successful() {
        jokeService.delete(ID);

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void like() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new JokeEntity()));

        jokeService.like(1L);

        verify(repository).findById(anyLong());
        verify(repository).save(any(JokeEntity.class));
    }

    @Test
    void getTop() {
        when(repository.findTop5ByOrderByLikesDesc()).thenReturn(Collections.singletonList(new JokeEntity()));

        var topJokes = jokeService.getTop();

        assertNotNull(topJokes);
        verify(repository, times(1)).findTop5ByOrderByLikesDesc();
    }

    @Test
    void getRandomJokeByCategory_successful() {
        when(repository.findAllByKategorie(JokeCategory.JOB)).thenReturn(Collections.singletonList(new JokeEntity()));

        var randomJoke = jokeService.getRandomJokeByCategory(JokeCategory.JOB);

        assertNotNull(randomJoke);
        verify(repository, times(1)).findAllByKategorie(JokeCategory.JOB);
    }

    @Test
    void getRandomJokeByCategory_exception() {
        when(repository.findAllByKategorie(JokeCategory.JOB)).thenReturn(Collections.emptyList());

        assertThrows(JokeNotFoundException.class, () -> {
            jokeService.getRandomJokeByCategory(JokeCategory.JOB);
        });
        verify(repository, times(1)).findAllByKategorie(JokeCategory.JOB);
    }

    @Test
    void getJokeOfTheDay_successful() {
        when(repository.findAllByKategorie(any())).thenReturn(Collections.singletonList(new JokeEntity()));

        var jokeOfTheDay = jokeService.getJokeOfTheDay();

        assertNotNull(jokeOfTheDay);
        verify(repository, times(1)).findAllByKategorie(any());
    }

    @Test
    void getJokeOfTheDay_exception() {
        when(repository.findAllByKategorie(any())).thenReturn(Collections.emptyList());

        assertThrows(JokeNotFoundException.class, () -> {
            jokeService.getJokeOfTheDay();
        });
        verify(repository, times(1)).findAllByKategorie(any());
    }
}