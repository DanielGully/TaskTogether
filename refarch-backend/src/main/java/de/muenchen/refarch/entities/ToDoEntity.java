package de.muenchen.refarch.entities;

import de.muenchen.refarch.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents a ToDoEntity.
 */
@Table(name = "ToDoEntity")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToDoEntity extends BaseEntity {
    @Column(nullable = false, length = 100)
    @NotNull @Size(min = 2, max = 100) private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @NotNull private Priority priority;

    @Column(name = "deadline_date")
    private LocalDate deadlineDatum;
}
