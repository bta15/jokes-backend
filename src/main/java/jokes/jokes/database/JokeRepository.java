package jokes.jokes.database;

import jdk.jfr.Category;
import jokes.jokes.database.entity.Joke;
import jokes.jokes.database.entity.JokeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {

    List<Joke> findAllByCategory(JokeCategory category);

    boolean existsByText(String text);

}
