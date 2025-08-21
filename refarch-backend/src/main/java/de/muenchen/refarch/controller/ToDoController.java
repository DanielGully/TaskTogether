package de.muenchen.refarch.controller;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.services.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class ToDoController {
    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ToDoResponseDTO> getToDo(@PathVariable UUID todoId, @RequestParam UUID userId) {
        ToDoResponseDTO todo = toDoService.getToDo(todoId, userId);
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<ToDoResponseDTO> createToDo(@RequestBody ToDoRequestDTO request, @RequestParam UUID userId) {
        ToDoResponseDTO createdToDo = toDoService.createToDo(request, userId);
        return ResponseEntity.ok(createdToDo);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<ToDoResponseDTO> updateToDo(@PathVariable UUID todoId, @RequestBody ToDoRequestDTO request, @RequestParam UUID userId) {
        ToDoResponseDTO updatedToDo = toDoService.updateToDo(request, todoId, userId);
        return ResponseEntity.ok(updatedToDo);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteToDo(@PathVariable UUID todoId, @RequestParam UUID userId) {
        toDoService.deleteToDo(todoId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/priority")
    public ResponseEntity<List<ToDoResponseDTO>> getToDosByPriority(@RequestParam Priority priority, @RequestParam UUID userId) {
        List<ToDoResponseDTO> todos = toDoService.getToDosByPriority(priority, userId);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<ToDoResponseDTO>> getAllToDosSortedByDeadline(@RequestParam UUID userId) {
        List<ToDoResponseDTO> todos = toDoService.getAllToDosSortedByDeadline(userId);
        return ResponseEntity.ok(todos);
    }
}
