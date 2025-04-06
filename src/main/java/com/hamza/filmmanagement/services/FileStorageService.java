package com.hamza.filmmanagement.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


// Service annoté avec @Service pour indiquer qu'il s'agit d'un composant Spring responsable du stockage de fichiers
@Service
public class FileStorageService {

    // Chemin du répertoire où les fichiers seront stockés. Ici, le dossier est nommé "uploads".
    private final String uploadDir = "uploads/";

    // === Méthode pour stocker un fichier ===
    // Cette méthode prend un fichier MultipartFile, le sauvegarde dans un répertoire spécifié et retourne son nom de fichier unique
    public String storeFile(MultipartFile file) throws IOException {

        // Assurer que le répertoire de téléchargement existe
        // On crée le répertoire "uploads" s'il n'existe pas déjà
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {  // Si le répertoire n'existe pas
            Files.createDirectories(uploadPath);  // Créer le répertoire
        }

        // Générer un nom de fichier unique
        // Utilisation d'un UUID pour générer un identifiant unique et préfixer avec le nom original du fichier pour éviter les conflits
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();  // Générer un nouveau nom unique

        // Définir le chemin complet où le fichier sera stocké dans le répertoire "uploads"
        Path filePath = uploadPath.resolve(fileName);  // Résoudre le chemin complet

        // Sauvegarder le fichier
        // On copie le fichier depuis le flux d'entrée (inputStream) dans le chemin de stockage défini
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);  // Si un fichier existe déjà à cet emplacement, il est remplacé

        // Retourner le nom du fichier (et non son chemin complet) pour une utilisation ultérieure
        return fileName;  // Le nom du fichier est renvoyé, cela permet de l'utiliser dans d'autres parties du code
    }

    // === Méthode pour supprimer un fichier ===
    // Cette méthode supprime un fichier en fonction de son nom de fichier
    public void deleteFile(String fileName) {
        // Construire le chemin complet du fichier à partir du nom fourni
        Path filePath = Paths.get(uploadDir).resolve(fileName);  // Résoudre le chemin du fichier à supprimer

        try {
            // Essayer de supprimer le fichier
            // La méthode deleteIfExists permet de supprimer le fichier s'il existe, sinon elle ne fait rien
            Files.deleteIfExists(filePath);  // Suppression du fichier
        } catch (IOException e) {
            // Si une erreur survient lors de la suppression, une exception est levée avec un message explicite
            throw new RuntimeException("Could not delete file: " + fileName, e);  // Lancer une exception si la suppression échoue
        }
    }
}

