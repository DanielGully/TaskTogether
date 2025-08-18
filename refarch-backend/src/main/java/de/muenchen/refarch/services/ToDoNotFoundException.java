package de.muenchen.refarch.services;

public class ToDoNotFoundException extends RuntimeException {
    public ToDoNotFoundException(String message) {
        super(message);
    }
}
