package com.hamza.filmmanagement.repositories;

import com.hamza.filmmanagement.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Cette annotation indique à Spring que cette interface est un composant DAO (Data Access Object) et qu'elle est responsable de l'accès aux données. Spring va automatiquement gérer cette interface comme un bean de type repository.
public interface ActorRepository extends JpaRepository<Actor, Long> {

    // JpaRepository fournit des méthodes de gestion de base de données sans avoir à les implémenter manuellement.
    // Cette interface étend JpaRepository, qui est une interface de Spring Data JPA, permettant d'effectuer des opérations CRUD (Create, Read, Update, Delete) de manière simple et sans code supplémentaire.

    // Signature de JpaRepository :
    // JpaRepository<Actor, Long> : cela signifie que nous allons gérer des entités de type `Actor` et utiliser `Long` comme type pour la clé primaire (ID).
    // Avec cette extension, l'interface ActorRepository bénéficie de plusieurs méthodes prédéfinies pour interagir avec la base de données, sans avoir à les définir explicitement.

    // Les méthodes disponibles grâce à JpaRepository incluent (mais ne sont pas limitées à) :

    // 1. `save(S entity)` : Cette méthode permet de sauvegarder ou de mettre à jour un acteur dans la base de données.
    //    Si l'entité existe déjà (basée sur la clé primaire), elle sera mise à jour. Sinon, une nouvelle entité sera insérée.
    //    Exemple d'utilisation :
    //    actorRepository.save(actor);

    // 2. `findById(ID id)` : Cette méthode permet de rechercher un acteur par son identifiant (clé primaire).
    //    Elle renvoie un `Optional<Actor>`, ce qui permet de vérifier si un acteur avec l'ID spécifié existe dans la base de données.
    //    Exemple d'utilisation :
    //    Optional<Actor> actor = actorRepository.findById(id);
    //    actor.ifPresent(actor -> { ... });

    // 3. `findAll()` : Cette méthode permet de récupérer tous les acteurs présents dans la base de données sous forme de liste.
    //    Exemple d'utilisation :
    //    List<Actor> actors = actorRepository.findAll();

    // 4. `deleteById(ID id)` : Cette méthode permet de supprimer un acteur en utilisant son identifiant (ID).
    //    Exemple d'utilisation :
    //    actorRepository.deleteById(id);

    // 5. `count()` : Cette méthode permet de compter le nombre total d'acteurs dans la base de données.
    //    Exemple d'utilisation :
    //    long count = actorRepository.count();

    // 6. `existsById(ID id)` : Cette méthode permet de vérifier si un acteur existe dans la base de données, basé sur son ID.
    //    Exemple d'utilisation :
    //    boolean exists = actorRepository.existsById(id);

    // 7. `deleteAll()` : Cette méthode permet de supprimer tous les acteurs de la base de données (généralement utilisée avec prudence).
    //    Exemple d'utilisation :
    //    actorRepository.deleteAll();

    // Grâce à l'héritage de JpaRepository, toutes ces méthodes sont automatiquement mises à disposition par Spring Data JPA,
    // et n'ont pas besoin d'être définies dans l'interface `ActorRepository`.

    // Cette interface permet ainsi d'effectuer les opérations de base sur les entités `Actor` avec une implémentation automatique par Spring Data JPA.
}
