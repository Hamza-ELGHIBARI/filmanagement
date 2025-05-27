package com.hamza.filmmanagement.integration.repositories;

import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.entities.Director;
import com.hamza.filmmanagement.entities.Film;
import com.hamza.filmmanagement.repositories.ActorRepository;
import com.hamza.filmmanagement.repositories.DirectorRepository;
import com.hamza.filmmanagement.repositories.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // pour utiliser H2
class ActorRepositoryIT {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Test
    void shouldCountFilmsByActorId() {
        Actor actor = new Actor(null, "leonardo", "decaprios", "Américain");
        Actor savedActor = actorRepository.save(actor);

        // 2. Crée un réalisateur
        Director director = new Director();
        director.setFirstName("First");
        director.setLastName("Last");
        director.setNationality("FR");
        directorRepository.save(director);

        // 3. Crée un film qui référence cet acteur
        Film film1 = new Film();
        film1.setTitle("Un film");
        film1.setDescription("Description");
        film1.setPoster("poster.jpg");
        film1.setReleaseDate(LocalDate.now());
        film1.setDirector(director);
        film1.setActors(Set.of(savedActor)); // Référence de l’acteur

        // 3. Crée un film qui référence cet acteur
        Film film2 = new Film();
        film2.setTitle("Un film");
        film2.setDescription("Description");
        film2.setPoster("poster.jpg");
        film2.setReleaseDate(LocalDate.now());
        film2.setDirector(director);
        film2.setActors(Set.of(savedActor)); // Référence de l’acteur

        filmRepository.save(film1);
        filmRepository.save(film2);

        // Appeler la méthode personnalisée
        long count = actorRepository.countFilmsByActorId(actor.getId());

        // Vérifier que l'acteur a bien joué dans 2 films
        assertEquals(2L, count);
    }
}
