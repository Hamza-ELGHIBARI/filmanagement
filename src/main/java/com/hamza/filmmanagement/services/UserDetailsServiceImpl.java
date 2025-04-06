package com.hamza.filmmanagement.services;


import com.hamza.filmmanagement.entities.User;
import com.hamza.filmmanagement.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
// Annotation indiquant que cette classe est un service Spring, utilisée pour la logique métier
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Dépendance vers le repository de l'utilisateur, injectée via le constructeur
    private final UserRepository userRepository;

    // Constructeur pour l'injection des dépendances (UserRepository)
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;  // L'instance de repository sera utilisée pour récupérer les données utilisateurs
    }

    // === Chargement de l'utilisateur par email pour l'authentification ===
    // Cette méthode est appelée par Spring Security lors de l'authentification pour charger un utilisateur à partir de son email.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Recherche de l'utilisateur dans la base de données par son email
        // Si l'utilisateur n'est pas trouvé, une exception est lancée pour indiquer l'absence de l'utilisateur
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // === Transformation des rôles de l'utilisateur en une liste de GrantedAuthority ===
        // Les rôles de l'utilisateur qui sont  des enums sont convertis en objets SimpleGrantedAuthority
        // Spring Security utilise ces objets pour gérer les autorisations et l'accès aux ressources
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString())) // Convertit l'énumération du rôle en String
                .collect(Collectors.toList());

        // === Retour de l'objet User de Spring Security ===
        // L'objet retourné est une instance de org.springframework.security.core.userdetails.User,
        // qui est la classe standard utilisée par Spring Security pour représenter un utilisateur authentifié.
        // Elle contient l'email, le mot de passe et les rôles de l'utilisateur sous forme de granted authorities.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // L'email de l'utilisateur, utilisé pour l'authentification
                user.getPassword(), // Le mot de passe de l'utilisateur, nécessaire pour la vérification d'authentification
                authorities // La liste des rôles sous forme de GrantedAuthority, définissant ce que l'utilisateur peut faire
        );
    }
}
