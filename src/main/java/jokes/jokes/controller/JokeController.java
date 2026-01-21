package jokes.jokes.controller;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.service.JokeImportService;
import jokes.jokes.service.JokeService;
import jokes.jokes.service.csv.CsvJoke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public List<CsvJoke> getAll() {
        return jokeService.getAllJokes();
    }

    @PostMapping("/import/csv")
    public ResponseEntity<String> importFromCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file_is_empty");
        }

        System.out.println("Importing CSV file " + file.getOriginalFilename());
        jokeImportService.importCsv(file);

        return ResponseEntity.ok().body("success");
    }

    @PutMapping("/{id}")
    public ResponseEntity<JokeDto> update(@PathVariable("id") Long id, @RequestBody JokeDto updateJoke) throws IOException {
        var updated = jokeService.update(id, updateJoke);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        jokeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
