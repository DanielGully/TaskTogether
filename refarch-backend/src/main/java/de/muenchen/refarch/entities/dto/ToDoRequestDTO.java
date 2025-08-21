package de.muenchen.refarch.entities.dto;

import de.muenchen.refarch.entities.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import java.time.LocalDate;

@Setter
@Getter
public class ToDoRequestDTO {
    @NotNull
    private String title;

    private String description;

    @NotNull
    private Priority priority;

    private LocalDate deadlineDatum;

    @NotNull
    private UUID userId;
}
