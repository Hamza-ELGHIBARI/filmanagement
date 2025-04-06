package com.hamza.filmmanagement.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FilmRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Release date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotEmpty(message = "At least one actor ID is required")
    private List<@NotNull Long> actorsIds;

    @NotNull(message = "Director ID is required")
    private Long directorId;

    @NotNull(message = "Poster is required")
    private MultipartFile poster;

}
