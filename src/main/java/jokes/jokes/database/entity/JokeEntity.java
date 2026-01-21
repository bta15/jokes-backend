package jokes.jokes.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name="joke" )
public class JokeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "witz")
    private String witz;

    @Enumerated(EnumType.STRING)
    @Column(name = "kategorie")
    private JokeCategory kategorie;
}
