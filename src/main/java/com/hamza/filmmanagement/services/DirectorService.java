package com.hamza.filmmanagement.services;

import com.hamza.filmmanagement.entities.Director;
import com.hamza.filmmanagement.exceptions.director.DirectorNotFoundException;
import com.hamza.filmmanagement.repositories.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public void saveDirector(Director director) {
        directorRepository.save(director);
    }

    public void updateDirector(Long id, Director director) {
        Director existingDirector = directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found"));
        existingDirector.setFirstName(director.getFirstName());
        existingDirector.setLastName(director.getLastName());
        existingDirector.setNationality(director.getNationality());
        directorRepository.save(existingDirector);
    }

    public void deleteDirector(Long id) {
        directorRepository.deleteById(id);
    }

    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    public Director getDirectorById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found"));
    }
}

