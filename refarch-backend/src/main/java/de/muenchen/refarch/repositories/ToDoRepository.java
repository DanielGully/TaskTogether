package de.muenchen.refarch.repositories;

import de.muenchen.refarch.entities.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ToDoRepository extends JpaRepository<ToDoEntity, UUID> {
}
