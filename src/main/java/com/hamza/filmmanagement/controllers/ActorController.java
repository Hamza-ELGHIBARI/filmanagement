package com.hamza.filmmanagement.controllers;

import com.hamza.filmmanagement.dto.ActorRequest;
import com.hamza.filmmanagement.dto.ApiResponse;
import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.services.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Annotation qui indique que cette classe est un contrôleur REST (elle renvoie des objets directement en JSON)
@RestController

// Préfixe d’URL commun pour toutes les routes définies ici. Toutes commenceront par /admin/actors
@RequestMapping("/admin/actors")
public class ActorController {

    // Injection du service métier qui contient la logique pour manipuler les acteurs (enregistrer, modifier, supprimer, etc.)
    private final ActorService actorService;

    // Constructeur avec injection automatique par Spring (grâce à @RestController ou @Component, etc.)
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // Route HTTP POST pour ajouter un nouvel acteur
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addActor(@Valid @RequestBody ActorRequest request) {
        // On crée un objet actor de type Actor à partir de la requete entrante
        Actor actor = new Actor();
        actor.setFirstName(request.getFirstName());
        actor.setLastName(request.getLastName());
        actor.setNationality(request.getNationality());

        // On délègue la sauvegarde de l’acteur au service
        actorService.saveActor(actor);

        // On retourne une réponse standardisée avec un message de succès et le code HTTP 200
        return ResponseEntity.ok(new ApiResponse<>("Actor added successfully", HttpStatus.OK.value()));
    }

    // Route HTTP PUT pour modifier un acteur existant (basé sur son id)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateActor(@PathVariable Long id,@Valid @RequestBody ActorRequest request) {
        // On crée un objet actor de type Actor à partir de la requete entrante
        Actor actor = new Actor();
        actor.setFirstName(request.getFirstName());
        actor.setLastName(request.getLastName());
        actor.setNationality(request.getNationality());

        // On transmet l'id et les nouvelles informations de l'acteur au service
        actorService.updateActor(id, actor);

        // Réponse confirmant la mise à jour
        return ResponseEntity.ok(new ApiResponse<>("Actor updated successfully", HttpStatus.OK.value()));
    }

    // Route HTTP DELETE pour supprimer un acteur
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteActor(@PathVariable Long id) {
        // Suppression de l’acteur via son identifiant
        actorService.deleteActor(id);

        // Retour d'une confirmation de suppression
        return ResponseEntity.ok(new ApiResponse<>("Actor deleted successfully", HttpStatus.OK.value()));
    }

    // Route HTTP GET pour récupérer tous les acteurs de la base
    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors() {
        // On récupère la liste des acteurs depuis le service, et on la retourne directement
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        Actor actor = actorService.getActorById(id);
        return actor;

    }
}
