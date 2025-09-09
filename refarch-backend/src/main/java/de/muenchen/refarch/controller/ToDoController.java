package de.muenchen.refarch.controller;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.security.AuthUtils;
import de.muenchen.refarch.security.Authorities;
import de.muenchen.refarch.services.ToDoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(Authorities.THEENTITY_GET)
    public ResponseEntity<ToDoResponseDTO> getToDo(@PathVariable UUID todoId, @RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        ToDoResponseDTO todo = toDoService.getToDo(todoId, userId);
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    @PreAuthorize(Authorities.THEENTITY_CREATE)
    public ResponseEntity<ToDoResponseDTO> createToDo(@RequestBody ToDoRequestDTO request, @RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        ToDoResponseDTO createdToDo = toDoService.createToDo(request, userId);
        return ResponseEntity.ok(createdToDo);
    }

    @PutMapping("/{todoId}")
    @PreAuthorize(Authorities.THEENTITY_UPDATE)
    public ResponseEntity<ToDoResponseDTO> updateToDo(@PathVariable UUID todoId, @RequestBody ToDoRequestDTO request, @RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        ToDoResponseDTO updatedToDo = toDoService.updateToDo(request, todoId, userId);
        return ResponseEntity.ok(updatedToDo);
    }

    @DeleteMapping("/{todoId}")
    @PreAuthorize(Authorities.THEENTITY_DELETE)
    public ResponseEntity<Void> deleteToDo(@PathVariable UUID todoId, @RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        toDoService.deleteToDo(todoId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/priority")
    @PreAuthorize(Authorities.THEENTITY_GET_ALL)
    public ResponseEntity<List<ToDoResponseDTO>> getToDosByPriority(@RequestParam Priority priority, @RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        List<ToDoResponseDTO> todos = toDoService.getToDosByPriority(priority, userId);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/sorted")
    @PreAuthorize(Authorities.THEENTITY_GET_ALL)
    public ResponseEntity<List<ToDoResponseDTO>> getAllToDosSortedByDeadline(@RequestParam UUID userId) {
        if(!userId.toString().equals(AuthUtils.getUserId())){
            System.out.println("YOUR NOT THAT USER!");
            return ResponseEntity.badRequest().build();
        }
        List<ToDoResponseDTO> todos = toDoService.getAllToDosSortedByDeadline(userId);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/admin")
    @PreAuthorize(Authorities.ADMIN_GET_ALL)
    public ResponseEntity<List<ToDoResponseDTO>> getAllToDosForAdmin() {
        System.out.println("DIESE_ID: " + AuthUtils.getUserId());
        List<ToDoResponseDTO> todos = toDoService.getAllToDosForAdmin();
        return ResponseEntity.ok(todos);
    }
}
