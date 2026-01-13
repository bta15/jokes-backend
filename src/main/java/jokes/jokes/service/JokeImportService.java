package jokes.jokes.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jokes.jokes.service.csv.CsvJoke;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JokeImportService {

    public void importJokes(InputStreamSource file) throws IOException {
        CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.WRAP_AS_ARRAY);
        CsvSchema schema = mapper.typedSchemaFor(CsvJoke.class).withHeader().withColumnSeparator(';');

        List<CsvJoke> imported = mapper.readerFor(CsvJoke.class)
                .with(schema)
                .<CsvJoke>readValues(file.getInputStream())
                .readAll();

        imported.forEach(System.out::println);
    }
}
