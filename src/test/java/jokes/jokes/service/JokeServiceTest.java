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
        //TODO
    }

    @Test
    void updateJoke_exception() {
        //TODO
    }

    @Test
    void deleteJoke_successful() {

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
        //TODO
    }

    @Test
    void getRandomJokeByCategory_successful() {
        //TODO
    }

    @Test
    void getRandomJokeByCategory_exception() {
        //TODO
    }

    @Test
    void getJokeOfTheDay_successful() {
        //TODO
    }

    @Test
    void getJokeOfTheDay_exception() {
        //TODO
    }
}