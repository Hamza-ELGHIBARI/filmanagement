package com.hamza.filmmanagement.repositories;

import com.hamza.filmmanagement.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findAll();
}
