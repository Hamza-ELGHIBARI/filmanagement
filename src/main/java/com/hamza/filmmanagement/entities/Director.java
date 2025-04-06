package com.hamza.filmmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;  // Director's first name
    private String lastName;  // Director's last name
    private String nationality;  // Director's nationality

    //@OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true)
    //private Set<Film> films = new HashSet<>();  // Films directed by this director

}
