package de.muenchen.refarch.services;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.exception.ToDoNotFoundException;
import de.muenchen.refarch.mapper.ToDoMapper;
import de.muenchen.refarch.repositories.ToDoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;

    public ToDoService(ToDoRepository toDoRepository, ToDoMapper toDoMapper) {
        this.toDoRepository = toDoRepository;
        this.toDoMapper = toDoMapper;
    }

    public ToDoResponseDTO getToDo(UUID todoId, UUID userId) {
        ToDoEntity todo = toDoRepository.findById(todoId)
                .filter(t -> t.getUserId().equals(userId))
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found with ID: " + todoId));

        return toDoMapper.toDTO(todo);
    }

    public ToDoResponseDTO createToDo(ToDoRequestDTO request, UUID userId) {
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title must not be null or empty.");
        }
        ToDoEntity toDoEntity = toDoMapper.toEntity(request);
        toDoEntity.setUserId(userId);
        ToDoEntity createdToDo = toDoRepository.save(toDoEntity);
        return toDoMapper.toDTO(createdToDo);
    }

    public ToDoResponseDTO updateToDo(ToDoRequestDTO request, UUID todoId, UUID userId) {
        ToDoEntity existingToDo = toDoRepository.findById(todoId)
                .filter(t -> t.getUserId().equals(userId))
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found with ID: " + todoId));

        existingToDo.setTitle(request.getTitle());
        existingToDo.setDescription(request.getDescription());
        existingToDo.setPriority(request.getPriority());
        existingToDo.setDeadlineDatum(request.getDeadlineDatum());

        ToDoEntity updatedToDo = toDoRepository.save(existingToDo);
        return toDoMapper.toDTO(updatedToDo);
    }

    public void deleteToDo(UUID todoId, UUID userId) {
        ToDoEntity todo = toDoRepository.findById(todoId)
                .filter(t -> t.getUserId().equals(userId))
                .orElseThrow(() -> new ToDoNotFoundException("ToDo not found with ID: " + todoId));

        toDoRepository.delete(todo);
    }

    public List<ToDoResponseDTO> getToDosByPriority(Priority priority, UUID userId) {
        List<ToDoEntity> todos = toDoRepository.findByPriorityAndUserId(priority, userId, Sort.by("deadlineDatum")
                .and(Sort.by("title").ascending()));
        return todos.stream()
                .map(toDoMapper::toDTO)
                .toList();
    }

    public List<ToDoResponseDTO> getAllToDosSortedByDeadline(UUID userId) {
        List<ToDoEntity> todos = toDoRepository.findByUserId(userId, Sort.by("deadlineDatum").ascending()
                .and(Sort.by("priority").ascending())
                .and(Sort.by("title").ascending()));
        return todos.stream()
                .map(toDoMapper::toDTO)
                .toList();
    }

    public List<ToDoResponseDTO> getAllToDosForAdmin() {
        List<ToDoEntity> todos = toDoRepository.findAll(Sort.by("userId").ascending()
                .and(Sort.by("deadlineDatum").ascending())
                .and(Sort.by("priority").ascending())
                .and(Sort.by("title").ascending()));
        return todos.stream()
                .map(toDoMapper::toDTO)
                .toList();
    }
}
