package com.hamza.filmmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;  // Rating score, e.g., 1 to 5 stars

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // User who gave the rating

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;  // Film being rated


}

