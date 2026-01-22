package jokes.jokes.controller.dto;

import jokes.jokes.database.entity.JokeCategory;
import org.springframework.http.HttpStatus;

public record ErrorDto(String title, String detail) {
}
