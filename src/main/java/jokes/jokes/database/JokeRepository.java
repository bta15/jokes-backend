package jokes.jokes.database;

import jokes.jokes.database.entity.JokeEntity;
import jokes.jokes.database.entity.JokeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JokeRepository extends JpaRepository<JokeEntity, Long> {

    List<JokeEntity> findAllByKategorie(JokeCategory category);

    boolean existsByWitz(String witz);

    List<JokeEntity> findTop5ByOrderByLikesDesc();

}
