package de.muenchen.refarch.controller;

import de.muenchen.refarch.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping("/generate-id")
    public Long generateNewId() {
        return toDoService.generateNewId();
    }
}
