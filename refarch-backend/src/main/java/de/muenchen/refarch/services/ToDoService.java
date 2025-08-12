package de.muenchen.refarch.services;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ToDoService {
    private AtomicLong idCounter = new AtomicLong(1);

    public Long generateNewId() {
        return idCounter.getAndIncrement();
    }
}
