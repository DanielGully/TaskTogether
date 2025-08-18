package de.muenchen.refarch.entities.dto;

import de.muenchen.refarch.entities.Priority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class ToDoResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadlineDatum;
}
