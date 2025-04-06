package com.hamza.filmmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // Film title
    private String description;  // Film description
    private String poster;  // Film poster image URL or path
    private LocalDate releaseDate;  // Film release date

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;  // Director of the film

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();  // Actors in the film

    @OneToMany(mappedBy = "film")
    private Set<Rating> ratings = new HashSet<>();  // Ratings for the film


}
