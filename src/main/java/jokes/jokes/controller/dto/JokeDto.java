package jokes.jokes.controller.dto;

import jokes.jokes.database.entity.JokeCategory;

public record JokeDto(String witz, JokeCategory kategorie) {
}
