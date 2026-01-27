package jokes.jokes.service;

import jokes.jokes.repository.JokeRepository;
import jokes.jokes.entity.JokeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeServiceTest {


    private JokeService jokeService;

    @Mock
    private JokeRepository repository;

    @BeforeEach
    void setUp() {
        jokeService = new JokeService(repository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    void like() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new JokeEntity()));

        jokeService.like(1L);

        verify(repository).findById(anyLong());
        verify(repository).save(any(JokeEntity.class));
    }

}