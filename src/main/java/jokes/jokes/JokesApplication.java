package jokes.jokes;

import jokes.jokes.database.JokeRepository;
import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JokesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokesApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(JokeRepository repository) {
		return (_) -> {
			Joke joke = new Joke();
			joke.setCategory(JokeCategory.GENERAL);
			joke.setText("Hello World!");
			repository.save(joke);
		};
	}
}
