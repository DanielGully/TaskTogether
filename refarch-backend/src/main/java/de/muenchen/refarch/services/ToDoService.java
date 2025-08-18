package de.muenchen.refarch.services;

import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.mapper.ToDoMapper;
import de.muenchen.refarch.repositories.ToDoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;

    public ToDoService(ToDoRepository toDoRepository, ToDoMapper toDoMapper) {
        this.toDoRepository = toDoRepository;
        this.toDoMapper = toDoMapper;
    }

    public ToDoEntity getToDo(UUID todoId) {
        return toDoRepository.findById(todoId)
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found with ID: " + todoId));
    }

    public Page<ToDoEntity> getAllToDos(int pageNumber, int pageSize) {
        return toDoRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber));
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

}
