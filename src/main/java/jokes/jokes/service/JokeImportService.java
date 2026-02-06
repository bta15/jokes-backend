package jokes.jokes.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jokes.jokes.repository.JokeRepository;
import jokes.jokes.entity.JokeEntity;
import jokes.jokes.entity.JokeCategory;
import jokes.jokes.service.csv.CsvJoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JokeImportService {

    @Autowired
    private JokeRepository jokeRepository;

    public void importCsv(InputStreamSource file) throws IOException {
        CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
        CsvSchema schema = mapper.typedSchemaFor(CsvJoke.class).withHeader().withColumnSeparator(';');

        List<CsvJoke> csvJokes = mapper.readerFor(CsvJoke.class)
                .with(schema)
                .<CsvJoke>readValues(file.getInputStream())
                .readAll();

        List<JokeEntity> newJokeEntityList = new ArrayList<>();
        csvJokes.forEach(newJoke -> {
            if (newJoke.getJoke() != null && !newJoke.getJoke().isEmpty() && jokeRepository.existsByWitz(newJoke.getJoke())) {
                return;
            }
            JokeCategory newCategory = JokeCategory.valueOf(newJoke.getCategory().toUpperCase());
            JokeEntity jokeEntity = new JokeEntity();
            jokeEntity.setKategorie(newCategory);
            jokeEntity.setWitz(newJoke.getJoke());
            newJokeEntityList.add(jokeEntity);
        });

        if (!newJokeEntityList.isEmpty()) {
            jokeRepository.saveAll(newJokeEntityList);
        }
    }
}
