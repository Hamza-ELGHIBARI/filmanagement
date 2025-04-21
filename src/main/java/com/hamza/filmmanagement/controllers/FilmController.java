package com.hamza.filmmanagement.controllers;

import com.hamza.filmmanagement.dto.ApiResponse;
import com.hamza.filmmanagement.dto.CreateFilmRequest;
import com.hamza.filmmanagement.dto.UpdateFilmRequest;
import com.hamza.filmmanagement.entities.Film;
import com.hamza.filmmanagement.services.ActorService;
import com.hamza.filmmanagement.services.FilmService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
// Indique que cette classe est un contrôleur REST (retourne des objets JSON au lieu de vues HTML)
@RestController

// Définit le préfixe de toutes les routes de ce contrôleur : /admin/films
@RequestMapping("/admin/films")
public class FilmController {

    // Dépendance vers le service métier des films, injectée via le constructeur
    private final FilmService filmService;

    // Dépendance vers le service métier des acteurs (utilisé indirectement si besoin)
    private final ActorService actorService;

    // Injection des dépendances via constructeur (pratique recommandée pour testabilité)
    public FilmController(FilmService filmService, ActorService actorService) {
        this.filmService = filmService;
        this.actorService = actorService;
    }

    // === Ajouter un nouveau film ===
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addFilm(
            // Utilisation de @ModelAttribute pour recevoir les données multipart (formulaire + fichier)
            @Valid @ModelAttribute CreateFilmRequest request) throws IOException {

        // Appel au service pour sauvegarder le film avec tous ses attributs
        filmService.saveFilm(
                request.getTitle(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getActorsIds(),      // Liste des ID d'acteurs à associer
                request.getDirectorId(),     // ID du réalisateur
                request.getPoster()          // Fichier image du poster
        );

        // Retourne une réponse 200 OK avec un message personnalisé
        return ResponseEntity.ok(new ApiResponse<>("Film added successfully", HttpStatus.OK.value()));
    }

    // === Modifier un film existant ===
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateFilm(@PathVariable Long id,
                                                          @Valid @ModelAttribute UpdateFilmRequest request) throws IOException {
        // Appel au service pour mettre à jour le film avec les nouvelles infos
        filmService.updateFilm(id, request.getTitle(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getActorsIds(),
                request.getDirectorId(),
                request.getPoster());

        // Retourne une réponse de succès avec un message clair
        return ResponseEntity.ok(new ApiResponse<>("film updated successfuly", HttpStatus.OK.value()));
    }

    // === Supprimer un film ===
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFilm(@PathVariable Long id) {
        // Appel au service pour supprimer le film par son ID
        filmService.deleteFilm(id);

        // Réponse avec message de confirmation
        return ResponseEntity.ok(new ApiResponse<>("Film deleted successfully", HttpStatus.OK.value()));
    }

    // === Récupérer tous les films ===
    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        // Appel au service pour récupérer la liste complète des films
        List<Film> films = filmService.getAllFilms();

        // Retour de la liste avec un code 200 OK
        return ResponseEntity.ok(films);
    }

    // === Récupérer un film par son ID ===
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        // Appel au service pour chercher le film correspondant
        Film film = filmService.getFilmById(id);

        // Retourne l'objet Film trouvé (ou lève une exception si non trouvé dans le service)
        return ResponseEntity.ok(film);
    }
}
