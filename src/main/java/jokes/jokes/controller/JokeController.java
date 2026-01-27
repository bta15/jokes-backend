package jokes.jokes.controller;

import jokes.jokes.controller.dto.JokeDto;
import jokes.jokes.entity.JokeCategory;
import jokes.jokes.entity.JokeEntity;
import jokes.jokes.service.JokeExportService;
import jokes.jokes.service.JokeImportService;
import jokes.jokes.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/jokes")
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @Autowired
    private JokeImportService jokeImportService;

    @Autowired
    private JokeExportService jokeExportService;

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

    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportCsvFile() throws IOException {
        var csvContent = jokeExportService.exportCsv();
        var filename = "witze_export_" + LocalDateTime.now() + ".csv";
        return ResponseEntity.ok().header("Content-Disposition", "filename=" + filename)
                .contentLength(csvContent.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(csvContent);
    }

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

    @GetMapping("/top")
    public List<JokeEntity> getTop() {
        return jokeService.getTop();
    }
}
