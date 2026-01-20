package jokes.jokes.controller;

import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.service.JokeImportService;
import jokes.jokes.service.JokeService;
import jokes.jokes.service.csv.CsvJoke;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController("api/joke")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @Autowired
    private JokeImportService jokeImportService;

    @GetMapping("/all")
    public List<CsvJoke> getAllJokes() {
        return jokeService.getAllJokes();
    }

    @PostMapping("/import/csv")
    public ResponseEntity<String> importJokesFromCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file_is_empty");
        }

        System.out.println("Importing CSV file " + file.getOriginalFilename());
        jokeImportService.importJokes(file);

        return ResponseEntity.ok().body("success");
    }
}
