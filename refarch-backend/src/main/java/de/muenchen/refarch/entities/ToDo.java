package de.muenchen.refarch.entities;

import de.muenchen.refarch.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class represents a ToDo entity.
 */
@Table(name = "ToDo")
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToDo extends BaseEntity {
    @Column(nullable = false, length = 100)
    @NotNull @Size(min = 2, max = 100) private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @NotNull private Priority priority;

    @Column(name = "deadline_date")
    private LocalDate deadlineDatum;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
