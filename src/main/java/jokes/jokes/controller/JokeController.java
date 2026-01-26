package jokes.jokes.controller;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.database.entity.JokeCategory;
import jokes.jokes.database.entity.JokeEntity;
import jokes.jokes.service.JokeImportService;
import jokes.jokes.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/jokes")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @Autowired
    private JokeImportService jokeImportService;

    @GetMapping("/all")
    public List<JokeEntity> getAll() {
        return jokeService.getAllJokes();
    }

    @GetMapping("/random")
    public JokeEntity getRandomByCategory(@RequestParam JokeCategory category) {
        return jokeService.getRandomJokeByCategory(category);
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

    //TODO add export as csv file

    @PostMapping
    public ResponseEntity<JokeEntity> create(@RequestBody JokeDto jokeDto) {
        return ResponseEntity.ok(jokeService.create(jokeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JokeEntity> update(@PathVariable("id") Long id, @RequestBody JokeDto updateJoke) {
        var updated = jokeService.update(id, updateJoke);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        jokeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/like")
    public ResponseEntity<JokeEntity> like(@PathVariable("id") Long id) {
        return ResponseEntity.ok(jokeService.like(id));
    }

    @GetMapping("/today")
    public JokeEntity getJokeOfTheDay() {
        return jokeService.getJokeOfTheDay();
    }
}
