package jokes.jokes.controller.dto;

import jokes.jokes.entity.JokeCategory;

public record JokeDto(String witz, JokeCategory kategorie) {
}
