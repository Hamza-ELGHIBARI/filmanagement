package com.hamza.filmmanagement.controllers;

import com.hamza.filmmanagement.dto.ApiResponse;
import com.hamza.filmmanagement.dto.DirectorRequest;
import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.entities.Director;
import com.hamza.filmmanagement.services.DirectorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    // Add a new director
    @PostMapping
    public ResponseEntity<ApiResponse<String>>  addDirector(@Valid @RequestBody DirectorRequest request) {
        Director director = new Director();
        director.setFirstName(request.getFirstName());
        director.setLastName(request.getLastName());
        director.setNationality(request.getNationality());
        directorService.saveDirector(director);
        return ResponseEntity.ok(new ApiResponse<>("Director added successfuly", HttpStatus.OK.value()));
    }

    // Update a director
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>>  updateDirector(@PathVariable Long id,@Valid @RequestBody DirectorRequest request) {
        Director director = new Director();
        director.setFirstName(request.getFirstName());
        director.setLastName(request.getLastName());
        director.setNationality(request.getNationality());
        directorService.updateDirector(id,director);
        return ResponseEntity.ok(new ApiResponse<>("director updated successfuly", HttpStatus.OK.value()));
    }

    // Delete a director
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>>  deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
        return ResponseEntity.ok(new ApiResponse<>("director deleted successfuly", HttpStatus.OK.value()));
    }

    // Get all directors
    @GetMapping
    public ResponseEntity<List<Director>> getAllDirectors() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    // Get director by ID
    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorById(id));
    }

}
