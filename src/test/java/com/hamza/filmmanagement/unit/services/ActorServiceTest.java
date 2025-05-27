package com.hamza.filmmanagement.unit.services;

import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.exceptions.actor.ActorNotFoundException;
import com.hamza.filmmanagement.exceptions.actor.ActorRefencedByFilmException;
import com.hamza.filmmanagement.repositories.ActorRepository;
import com.hamza.filmmanagement.services.ActorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveActor_shouldCallRepositorySave() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");
        actor.setNationality("American");

        // when
        actorService.saveActor(actor);

        // then
        verify(actorRepository, times(1)).save(actor);
    }

    @Test
    void updateActor_shouldUpdateExistingActor() {
        // given
        Long id = 1L;
        Actor existing = new Actor(id, "Old", "Name", "British");
        Actor updated = new Actor();
        updated.setFirstName("New");
        updated.setLastName("Name");
        updated.setNationality("Canadian");

        when(actorRepository.findById(id)).thenReturn(Optional.of(existing));

        // when
        actorService.updateActor(id, updated);

        // then
        assertThat(existing.getFirstName()).isEqualTo("New");
        assertThat(existing.getNationality()).isEqualTo("Canadian");
        verify(actorRepository,times(1)).save(existing);
    }

    @Test
    void updateActor_shouldThrowExceptionWhenActorNotFound() {
        // given
        Long id = 99L;
        Actor updated = new Actor();
        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> actorService.updateActor(id, updated))
                .isInstanceOf(ActorNotFoundException.class)
                .hasMessage("Actor not found");

        verify(actorRepository, never()).save(any());
    }

    @Test
    void deleteActor_shouldCallDeleteById() {
        // given
        Long id = 1L;

        // when
        actorService.deleteActor(id);

        // then
        verify(actorRepository).deleteById(id);
    }

    @Test
    void deleteActor_shouldThrowExceptionIfActorIsReferenced() {
        // given
        Long id = 1L;
        doThrow(DataIntegrityViolationException.class).when(actorRepository).deleteById(id);

        // when / then
        assertThatThrownBy(() -> actorService.deleteActor(id))
                .isInstanceOf(ActorRefencedByFilmException.class)
                .hasMessageContaining("Cannot delete actor");

        verify(actorRepository).deleteById(id);
    }

    @Test
    void getAllActors_shouldReturnAllActors() {
        // given
        List<Actor> actors = Arrays.asList(
                new Actor(1L, "A", "B", "FR"),
                new Actor(2L, "C", "D", "US")
        );
        when(actorRepository.findAll()).thenReturn(actors);

        // when
        List<Actor> result = actorService.getAllActors();

        // then
        assertThat(result).hasSize(2);
        verify(actorRepository).findAll();
    }

    @Test
    void getActorById_shouldReturnActorIfExists() {
        // given
        Long id = 1L;
        Actor actor = new Actor(id, "Tom", "Hanks", "US");
        when(actorRepository.findById(id)).thenReturn(Optional.of(actor));

        // when
        Actor result = actorService.getActorById(id);

        // then
        assertThat(result).isEqualTo(actor);
    }

    @Test
    void getActorById_shouldThrowIfNotFound() {
        // given
        Long id = 999L;
        when(actorRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> actorService.getActorById(id))
                .isInstanceOf(ActorNotFoundException.class)
                .hasMessage("Actor not found");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
