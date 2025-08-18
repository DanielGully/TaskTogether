package de.muenchen.refarch.entities.dto;

import de.muenchen.refarch.entities.Priority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ToDoRequestDTO {
    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    private String description;

    @NotNull
    private Priority priority;

    private LocalDate deadlineDatum;

}
