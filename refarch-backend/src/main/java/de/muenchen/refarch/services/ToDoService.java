package de.muenchen.refarch.services;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.repositories.ToDoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public ToDoEntity getToDo(UUID todoId) {
        return toDoRepository.findById(todoId)
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found with ID: " + todoId));
    }

    public ToDoEntity createToDo(ToDoEntity toDoEntity) {
        System.out.println("Creating Todo...");
        return toDoRepository.save(toDoEntity);
    }

    public ToDoEntity updateToDo(ToDoEntity toDoEntity, UUID todoId) {
        if (!toDoRepository.existsById(todoId)) {
            throw new ToDoNotFoundException("ToDo not found with ID: " + todoId);
        }
        toDoEntity.setId(todoId);
        return toDoRepository.save(toDoEntity);
    }

    public void deleteToDo(UUID todoId) {
        if (!toDoRepository.existsById(todoId)) {
            throw new ToDoNotFoundException("ToDo not found with ID: " + todoId);
        }
        toDoRepository.deleteById(todoId);
    }

    public List<ToDoEntity> getAllToDos() {
        return toDoRepository.findAll();
    }

    public List<ToDoEntity> getToDosByPriority(Priority priority) {
        return toDoRepository.findByPriority(priority, Sort.by("deadlineDatum"));
    }
}
