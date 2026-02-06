package jokes.jokes.service;

import jokes.jokes.entity.JokeCategory;
import jokes.jokes.entity.JokeEntity;
import jokes.jokes.repository.JokeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeExportServiceTest {

    @InjectMocks
    private JokeExportService jokeExportService;

    @Mock
    private JokeRepository jokeRepository;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    @SneakyThrows
    void export() {
        var allJokes = Collections.singletonList(new JokeEntity(1L, "witz", JokeCategory.JOB, 4));
        when(jokeRepository.findAll()).thenReturn(allJokes);

        var exported = jokeExportService.exportCsv();
        var exportedString = new String(exported.getInputStream().readAllBytes());
        assertEquals("witz;kategorie\r\nwitz;JOB\r\n", exportedString);

        verify(jokeRepository, times(1)).findAll();
    }

}