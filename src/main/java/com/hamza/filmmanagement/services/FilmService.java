package com.hamza.filmmanagement.services;

import com.hamza.filmmanagement.entities.Actor;
import com.hamza.filmmanagement.entities.Director;
import com.hamza.filmmanagement.entities.Film;
import com.hamza.filmmanagement.exceptions.actor.ActorNotFoundException;
import com.hamza.filmmanagement.exceptions.director.DirectorNotFoundException;
import com.hamza.filmmanagement.exceptions.film.FilmNotFoundException;
import com.hamza.filmmanagement.repositories.ActorRepository;
import com.hamza.filmmanagement.repositories.DirectorRepository;
import com.hamza.filmmanagement.repositories.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
// Classe annotée comme un service Spring, fournissant la logique métier pour gérer les films
@Service
public class FilmService {

    // Dépendances injectées via le constructeur :
    // - FilmRepository : pour interagir avec la base de données concernant les films
    // - ActorRepository : pour interagir avec les acteurs
    // - DirectorRepository : pour interagir avec les réalisateurs
    // - FileStorageService : pour gérer le stockage des fichiers, comme les affiches de films
    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final FileStorageService fileStorageService;

    // Constructeur pour injecter les dépendances nécessaires
    public FilmService(FilmRepository filmRepository, ActorRepository actorRepository, DirectorRepository directorRepository, FileStorageService fileStorageService, EmailService emailService) {
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.fileStorageService = fileStorageService;
    }

    // === Méthode pour ajouter un film ===
    // Cette méthode crée un nouveau film et l'enregistre dans la base de données

    public void saveFilm(String title, String description, LocalDate releaseDate, List<Long> actorsIds, Long directorId, MultipartFile poster) throws IOException {
        // Création d'un objet Film avec les informations de base
        Film film = new Film();
        film.setTitle(title);  // Titre du film
        film.setDescription(description);  // Description du film
        film.setReleaseDate(releaseDate);  // Date de sortie du film

        // Récupérer le réalisateur à partir de l'ID fourni
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found"));  // Si le réalisateur n'existe pas, on lance une exception
        film.setDirector(director);  // Assigner le réalisateur au film

        // Récupérer les acteurs à partir des IDs fournis
        Set<Actor> actors = actorsIds.stream()  // Convertir la liste des IDs des acteurs en un ensemble d'acteurs
                .map(actorId -> actorRepository.findById(actorId)  // Recherche de chaque acteur par ID
                        .orElseThrow(() -> new ActorNotFoundException("Actor not found ")))  // Si un acteur n'est pas trouvé, on lance une exception
                .collect(Collectors.toSet());  // Collecter les acteurs dans un ensemble
        film.setActors(actors);  // Assigner les acteurs au film

        // Upload de l'affiche et définition du nom du fichier
        String posterFileName = fileStorageService.storeFile(poster);  // Utilisation du service de stockage pour enregistrer le fichier
        film.setPoster(posterFileName);  // Attribuer le nom du fichier à l'entité Film

        // Sauvegarder le film dans la base de données
        filmRepository.save(film);
    }

    // === Méthode pour mettre à jour un film existant ===
    // Cette méthode permet de mettre à jour les informations d'un film existant

    public void updateFilm(Long filmId, String title, String description, LocalDate releaseDate, List<Long> actorsIds, Long directorId, MultipartFile poster) throws IOException {
        // Recherche du film existant par son ID
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Film not found with ID: " + filmId));  // Si le film n'existe pas, on lance une exception

        // Mise à jour des informations de base du film
        film.setTitle(title);  // Mise à jour du titre
        film.setDescription(description);  // Mise à jour de la description
        film.setReleaseDate(releaseDate);  // Mise à jour de la date de sortie

        // Mise à jour du réalisateur
        Director director = directorRepository.findById(directorId)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found"));  // Si le réalisateur n'est pas trouvé, on lance une exception
        film.setDirector(director);  // Assigner le réalisateur mis à jour

        // Mise à jour des acteurs
        Set<Actor> actors = actorsIds.stream()  // Transformer la liste des IDs en un ensemble d'acteurs
                .map(actorId -> actorRepository.findById(actorId)  // Recherche de chaque acteur par ID
                        .orElseThrow(() -> new ActorNotFoundException("Actor not found with ID: " + actorId)))  // Si un acteur n'est pas trouvé, on lance une exception
                .collect(Collectors.toSet());  // Collecte les acteurs dans un ensemble
        film.setActors(actors);  // Assigner les acteurs mis à jour au film

        // Gestion de la mise à jour de l'affiche
        if (poster != null && !poster.isEmpty()) {  // Si une nouvelle affiche est fournie
            // Suppression de l'ancienne affiche
            if (film.getPoster() != null) {
                fileStorageService.deleteFile(film.getPoster());  // Supprimer le fichier de l'ancienne affiche
            }

            // Upload de la nouvelle affiche
            String newPosterFileName = fileStorageService.storeFile(poster);  // Enregistrer la nouvelle affiche
            film.setPoster(newPosterFileName);  // Assigner le nom du fichier de la nouvelle affiche au film
        }

        // Sauvegarder les modifications du film dans la base de données
        filmRepository.save(film);
    }

    // === Méthode pour supprimer un film ===
    // Cette méthode supprime un film de la base de données, ainsi que son affiche

    public void deleteFilm(Long id) {
        // Recherche du film à supprimer par son ID
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException("Film not found with ID: " + id));  // Si le film n'est pas trouvé, on lance une exception

        // Suppression de l'affiche du film
        if (film.getPoster() != null) {
            Path posterPath = Paths.get("uploads", film.getPoster());  // Construire le chemin du fichier d'affiche
            try {
                Files.deleteIfExists(posterPath);  // Supprimer le fichier d'affiche s'il existe
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete poster file: " + film.getPoster(), e);  // Si une erreur se produit, on la lance
            }
        }

        // Suppression du film de la base de données
        filmRepository.deleteById(id);
    }

    // === Méthode pour récupérer tous les films ===
    // Cette méthode retourne tous les films enregistrés dans la base de données
    public List<Film> getAllFilms() {
        return filmRepository.findAll();  // Retourne la liste des films
    }

    // === Méthode pour récupérer un film par ID ===
    // Cette méthode permet de récupérer un film spécifique en fonction de son ID
    public Film getFilmById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new FilmNotFoundException("Film not found"));  // Si le film n'est pas trouvé, on lance une exception
    }
}
