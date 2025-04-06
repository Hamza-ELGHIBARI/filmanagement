package com.hamza.filmmanagement.repositories;

import com.hamza.filmmanagement.entities.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}
