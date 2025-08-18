package de.muenchen.refarch.controller;

import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.mapper.ToDoMapper;
import de.muenchen.refarch.services.ToDoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("todos")
public class ToDoController {

    private final ToDoService toDoService;
    private final ToDoMapper toDoMapper;

    /**
     * Retrieve a todoEntity by its UUID.
     * Fetches the todoEntity details using the provided UUID.
     *
     * @param todoId the UUID of the requested todoEntity
     * @return the todoEntity with the given UID as a DTO
     */
    @GetMapping("{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public ToDoResponseDTO getToDo(@PathVariable("todoId") final UUID todoId) {
        return toDoMapper.toDTO(toDoService.getToDo(todoId));
    }

    /**
     * Create a new todoEntity.
     * Creates a new todoEntity using the provided todoEntity details.
     *
     * @param toDoRequestDTO the details of the todoEntity to create
     * @return the created todoEntity as a DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToDoResponseDTO saveToDo(@Valid @RequestBody final ToDoRequestDTO toDoRequestDTO) {
        return toDoMapper.toDTO(toDoService.createToDo(toDoMapper.toEntity(toDoRequestDTO)));
    }

    /**
     * Update an existing todoEntity.
     * Updates the details of an existing todoEntity using the provided UUID and todoEntity details.
     *
     * @param toDoRequestDTO the new details of the todoEntity
     * @param todoId the UUID of the todoEntity to update
     * @return the updated todoEntity as a DTO
     */
    @PutMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public ToDoResponseDTO updateToDo(@Valid @RequestBody final ToDoRequestDTO toDoRequestDTO,
                                      @PathVariable("todoId") final UUID todoId) {
        return toDoMapper.toDTO(toDoService.updateToDo(toDoMapper.toEntity(toDoRequestDTO), todoId));
    }

    /**
     * Delete a todoEntity.
     * Deletes the todoEntity using the provided UUID.
     *
     * @param todoId the UUID of the todoEntity to delete
     */
    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteToDo(@PathVariable("todoId") final UUID todoId) {
        toDoService.deleteToDo(todoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoResponseDTO> getAllToDos() {
        return toDoService.getAllToDos().stream()
                .map(toDoMapper::toDTO)
                .collect(Collectors.toList());
    }
}

