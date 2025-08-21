package de.muenchen.refarch.repositories;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface ToDoRepository extends JpaRepository<ToDoEntity, UUID> {
    List<ToDoEntity> findByUserId(UUID userId, Sort sort);

    List<ToDoEntity> findByPriorityAndUserId(Priority priority, UUID userId, Sort deadlineDatum);
}