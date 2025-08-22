package de.muenchen.refarch.repositories;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ToDoRepositoryTest {

    @Mock
    private ToDoRepository toDoRepositoryMock;

    private UUID userId;
    private List<ToDoEntity> toDoEntities;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();

        toDoEntities = new ArrayList<>();
        ToDoEntity todo1 = new ToDoEntity();
        todo1.setUserId(userId);
        todo1.setPriority(Priority.HOCH);
        toDoEntities.add(todo1);

        ToDoEntity todo2 = new ToDoEntity();
        todo2.setUserId(userId);
        todo2.setPriority(Priority.NIEDRIG);
        toDoEntities.add(todo2);
    }

    @Test
    public void testFindByUserId() {
        when(toDoRepositoryMock.findByUserId(userId, Sort.by("deadlineDatum"))).thenReturn(toDoEntities);

        List<ToDoEntity> result = toDoRepositoryMock.findByUserId(userId, Sort.by("deadlineDatum"));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(toDoRepositoryMock, times(1)).findByUserId(userId, Sort.by("deadlineDatum"));
    }

    @Test
    public void testFindByPriorityAndUserId() {
        when(toDoRepositoryMock.findByPriorityAndUserId(Priority.HOCH, userId, Sort.by("deadlineDatum")))
                .thenReturn(List.of(toDoEntities.getFirst()));

        List<ToDoEntity> result = toDoRepositoryMock.findByPriorityAndUserId(Priority.HOCH, userId, Sort.by("deadlineDatum"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Priority.HOCH, result.getFirst().getPriority());
        verify(toDoRepositoryMock, times(1)).findByPriorityAndUserId(Priority.HOCH, userId, Sort.by("deadlineDatum"));
    }
}
