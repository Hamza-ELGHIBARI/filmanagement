package com.hamza.filmmanagement.services;

import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.exceptions.actor.ActorNotFoundException;
import com.hamza.filmmanagement.exceptions.actor.ActorRefencedByFilmException;
import com.hamza.filmmanagement.repositories.ActorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Annotation qui désigne cette classe comme un service Spring. Spring va gérer l'instanciation de cette classe.
public class ActorService {

    // Dépendance à l'ActorRepository pour interagir avec la base de données.
    // ActorRepository est une interface qui étend JpaRepository ou une autre interface Spring Data pour accéder aux acteurs.
    private final ActorRepository actorRepository;

    // Constructeur permettant l'injection de dépendances dans la classe.
    // Spring va automatiquement injecter une instance de ActorRepository lorsque cette classe est instanciée.
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    // Méthode permettant d'ajouter un nouvel acteur à la base de données.
    public void saveActor(Actor actor) {
        // La méthode 'save' de JpaRepository permet de sauvegarder un acteur dans la base de données.
        actorRepository.save(actor);
    }

    // Méthode permettant de mettre à jour un acteur existant dans la base de données.
    public void updateActor(Long id, Actor actor) {
        // Recherche de l'acteur par son identifiant dans la base de données.
        // Si l'acteur n'est pas trouvé, une exception RuntimeException est lancée.
        Actor existingActor = actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException("Actor not found"));

        // Mise à jour des informations de l'acteur avec les nouvelles données.
        existingActor.setFirstName(actor.getFirstName());
        existingActor.setLastName(actor.getLastName());
        existingActor.setNationality(actor.getNationality());

        // Sauvegarde de l'acteur mis à jour dans la base de données.
        actorRepository.save(existingActor);
    }

    // Méthode permettant de supprimer un acteur de la base de données en utilisant son identifiant.
    public void deleteActor(Long id) {
        try {
            actorRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ActorRefencedByFilmException("Cannot delete actor: still referenced by one or more films");
        }
    }

    // Méthode permettant de récupérer la liste de tous les acteurs.
    // Cette méthode retourne une liste de tous les acteurs présents dans la base de données.
    public List<Actor> getAllActors() {
        // La méthode 'findAll' de JpaRepository permet de récupérer tous les enregistrements d'acteurs.
        return actorRepository.findAll();
    }

    public Actor getActorById(Long id) {
         Actor actor= actorRepository.findById(id).orElseThrow(() -> new ActorNotFoundException("Actor not found"));
         return actor;
    }
}
