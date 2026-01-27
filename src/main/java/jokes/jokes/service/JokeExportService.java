package jokes.jokes.service;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.database.entity.JokeEntity;
import jokes.jokes.service.csv.CsvJoke;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class JokeExportService {

    @Autowired
    private JokeRepository jokeRepository;

    public ByteArrayResource exportCsv() throws IOException {
        var jokes = jokeRepository.findAll();
        if (jokes.isEmpty()) {
            throw new JokeNotFoundException("Jokes not found");
        }

        var csvJokes = jokes.stream().map(j -> {
            var csvJoke = new CsvJoke();
            csvJoke.setCategory(j.getKategorie().name());
            csvJoke.setJoke(j.getWitz());
            return csvJoke;
        }).toList();

        CsvMapper mapper = new CsvMapper().configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true);
        CsvSchema schema = mapper.schemaFor(CsvJoke.class).withHeader().withColumnSeparator(';').withLineSeparator("\r\n");
        var csvAsString = mapper.writer(schema).writeValueAsString(csvJokes);
        log.info(csvAsString);
        return new ByteArrayResource(csvAsString.getBytes());// todo check encoding ENCODING_WINDOWS_1252?
    }
}
