package jokes.jokes.service;

import jokes.jokes.repository.JokeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeImportServiceTest {

    @InjectMocks
    private JokeImportService jokeImportService;

    @Mock
    private JokeRepository jokeRepository;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(jokeRepository);
    }

    @Test
    @SneakyThrows
    void importJoke_when_db_empty() {
        var url = getClass().getClassLoader().getResource("import/jokes.csv");
        var mockMultipartFile = createMockMultipartFile(url, "jokes.csv");
        when(jokeRepository.existsByWitz(any())).thenReturn(false);

        jokeImportService.importCsv(mockMultipartFile);

        verify(jokeRepository, times(2)).existsByWitz(any());
        verify(jokeRepository, times(1)).saveAll(any());
    }

    @Test
    @SneakyThrows
    void importJoke_when_db_not_empty() {
        var url = getClass().getClassLoader().getResource("import/jokes.csv");
        var mockMultipartFile = createMockMultipartFile(url, "jokes.csv");
        when(jokeRepository.existsByWitz(any())).thenReturn(true);

        jokeImportService.importCsv(mockMultipartFile);

        verify(jokeRepository, times(2)).existsByWitz(any());
    }

    @SneakyThrows
    private MockMultipartFile createMockMultipartFile(URL url, String filename) {
        var path = Paths.get(Objects.requireNonNull(url).toURI());
        return new MockMultipartFile("file", filename, "text/plain", Files.readAllBytes(path));
    }
}