package com.hamza.filmmanagement.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamza.filmmanagement.dto.ActorRequest;
import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.entities.Director;
import com.hamza.filmmanagement.entities.Film;
import com.hamza.filmmanagement.exceptions.actor.ActorNotFoundException;
import com.hamza.filmmanagement.repositories.ActorRepository;
import com.hamza.filmmanagement.repositories.DirectorRepository;
import com.hamza.filmmanagement.repositories.FilmRepository;
import com.hamza.filmmanagement.services.ActorService;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private FilmRepository filmRepository;

    @BeforeEach
    void setup() {
      filmRepository.deleteAll();
      actorRepository.deleteAll(); // Nettoie avant chaque test
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllActors_shouldReturnListOfActors() throws Exception {
        actorRepository.save(new Actor(null, "Tom", "Hanks", "US"));
        actorRepository.save(new Actor(null, "Emma", "Watson", "UK"));

        mockMvc.perform(get("/admin/actors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Tom"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getActorById_shouldReturnActor() throws Exception {
        Actor saved = actorRepository.save(new Actor(null, "Tom", "Hanks", "US"));

        mockMvc.perform(get("/admin/actors/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Tom"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getActorById_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(get("/admin/actors/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createActor_shouldReturn200() throws Exception {
        ActorRequest request = new ActorRequest();
        request.setFirstName("Brad");
        request.setLastName("Pitt");
        request.setNationality("US");

        mockMvc.perform(post("/admin/actors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor added successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateActor_shouldReturn200() throws Exception {
        Actor saved = actorRepository.save(new Actor(null, "Leo", "Old", "US"));

        ActorRequest request = new ActorRequest();
        request.setFirstName("Leonardo");
        request.setLastName("DiCaprio");
        request.setNationality("US");

        mockMvc.perform(put("/admin/actors/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor updated successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateActor_shouldReturn404IfActorNotFound() throws Exception {
        ActorRequest request = new ActorRequest();
        request.setFirstName("X");
        request.setLastName("Y");
        request.setNationality("Z");

        mockMvc.perform(put("/admin/actors/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteActor_shouldReturn200() throws Exception {
        Actor saved = actorRepository.save(new Actor(null, "Tom", "Cruise", "US"));

        mockMvc.perform(delete("/admin/actors/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor deleted successfully"));
    }

    // Ce test suppose que ton service empêche de supprimer un acteur lié à un film
    @Test
     @WithMockUser(username = "admin", roles = {"ADMIN"})
     void deleteActor_shouldReturn400IfReferenced() throws Exception {
        // 1. Crée un acteur
        Actor actor = new Actor(null, "Impossible", "ToDelete", "FR");
        Actor savedActor = actorRepository.save(actor);

        // 2. Crée un réalisateur
       Director director = new Director();
       director.setFirstName("First");
     director.setLastName("Last");
     director.setNationality("FR");
       directorRepository.save(director);

        // 3. Crée un film qui référence cet acteur
      Film film = new Film();
      film.setTitle("Un film");
       film.setDescription("Description");
       film.setPoster("poster.jpg");
       film.setReleaseDate(LocalDate.now());
       film.setDirector(director);
       film.setActors(Set.of(savedActor)); // Référence de l’acteur

      filmRepository.save(film);

        // 4. Tentative de suppression de l’acteur référencé
     mockMvc.perform(delete("/admin/actors/" + savedActor.getId()))
             .andDo(print())
             .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Cannot delete actor: still referenced by one or more films"));
    }

}
