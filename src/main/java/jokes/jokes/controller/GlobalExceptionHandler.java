package jokes.jokes.controller;

import jokes.jokes.controller.dto.ErrorDto;
import jokes.jokes.service.exception.JokeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(JokeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleJokeNotFound(JokeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("not found", e.getMessage()));
    }
}
