package jokes.jokes.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JokeAlreadyExistsException extends RuntimeException {

    public JokeAlreadyExistsException(String message) {
        super(message);
    }
}
