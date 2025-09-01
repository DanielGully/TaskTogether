package de.muenchen.refarch.services;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.exception.ToDoNotFoundException;
import de.muenchen.refarch.mapper.ToDoMapper;
import de.muenchen.refarch.repositories.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ToDoServiceTest {

    @Mock
    private ToDoRepository toDoRepository;

    @Mock
    private ToDoMapper toDoMapper;

    @InjectMocks
    private ToDoService toDoService;

    private UUID userId;
    private UUID todoId;
    private ToDoEntity todoEntity;
    private ToDoRequestDTO todoRequestDTO;
    private ToDoResponseDTO todoResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        todoId = UUID.randomUUID();

        todoEntity = new ToDoEntity();
        todoEntity.setId(todoId);
        todoEntity.setUserId(userId);
        todoEntity.setTitle("Test ToDo");
        todoEntity.setDescription("Test Description");
        todoEntity.setPriority(Priority.HOCH);
        todoEntity.setDeadlineDatum(LocalDate.now());

        todoRequestDTO = new ToDoRequestDTO();
        todoRequestDTO.setTitle("Test ToDo");
        todoRequestDTO.setDescription("Test Description");
        todoRequestDTO.setPriority(Priority.HOCH);
        todoRequestDTO.setDeadlineDatum(LocalDate.now());
        todoRequestDTO.setUserId(userId);

        todoResponseDTO = new ToDoResponseDTO();
        todoResponseDTO.setId(todoId);
        todoResponseDTO.setTitle("Test ToDo");
        todoResponseDTO.setDescription("Test Description");
        todoResponseDTO.setPriority(Priority.HOCH);
        todoResponseDTO.setDeadlineDatum(LocalDate.now());
    }

    @Test
    public void testGetToDo_Success() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));
        when(toDoMapper.toDTO(todoEntity)).thenReturn(todoResponseDTO);

        ToDoResponseDTO result = toDoService.getToDo(todoId, userId);

        assertNotNull(result);
        assertEquals(todoId, result.getId());
        verify(toDoRepository).findById(todoId);
        verify(toDoMapper).toDTO(todoEntity);
    }

    @Test
    public void testGetToDo_NotFound() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> toDoService.getToDo(todoId, userId));
    }

    @Test
    public void testCreateToDo_Success() {
        when(toDoMapper.toEntity(todoRequestDTO)).thenReturn(todoEntity);
        when(toDoRepository.save(todoEntity)).thenReturn(todoEntity);
        when(toDoMapper.toDTO(todoEntity)).thenReturn(todoResponseDTO);

        ToDoResponseDTO result = toDoService.createToDo(todoRequestDTO, userId);

        assertNotNull(result);
        assertEquals(todoId, result.getId());
        verify(toDoMapper).toEntity(todoRequestDTO);
        verify(toDoRepository).save(todoEntity);
    }

    @Test
    public void testUpdateToDo_Success() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));
        when(toDoRepository.save(todoEntity)).thenReturn(todoEntity);
        when(toDoMapper.toDTO(todoEntity)).thenReturn(todoResponseDTO);

        ToDoResponseDTO result = toDoService.updateToDo(todoRequestDTO, todoId, userId);

        assertNotNull(result);
        assertEquals(todoId, result.getId());
        verify(toDoRepository).findById(todoId);
        verify(toDoRepository).save(todoEntity);
    }

    @Test
    public void testUpdateToDo_NotFound() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> toDoService.updateToDo(todoRequestDTO, todoId, userId));
    }

    @Test
    public void testDeleteToDo_Success() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));

        assertDoesNotThrow(() -> toDoService.deleteToDo(todoId, userId));
        verify(toDoRepository).delete(todoEntity);
    }

    @Test
    public void testDeleteToDo_NotFound() {
        when(toDoRepository.findById(todoId)).thenReturn(Optional.empty());

        assertThrows(ToDoNotFoundException.class, () -> toDoService.deleteToDo(todoId, userId));
    }

    @Test
    public void testGetToDosByPriority_Success() {
        Priority priority = Priority.HOCH;
        List<ToDoEntity> todoEntities = List.of(todoEntity);

        when(toDoRepository.findByPriorityAndUserId(priority, userId, Sort.by("deadlineDatum"))).thenReturn(todoEntities);
        when(toDoMapper.toDTO(todoEntity)).thenReturn(todoResponseDTO);

        List<ToDoResponseDTO> result = toDoService.getToDosByPriority(priority, userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(todoId, result.getFirst().getId());
        verify(toDoRepository).findByPriorityAndUserId(priority, userId, Sort.by("deadlineDatum"));
    }

    @Test
    public void testGetAllToDosSortedByDeadline_Success() {
        List<ToDoEntity> todoEntities = List.of(todoEntity);

        when(toDoRepository.findByUserId(userId, Sort.by("deadlineDatum").ascending()
                .and(Sort.by("priority").ascending())
                .and(Sort.by("title").ascending()))).thenReturn(todoEntities);
        when(toDoMapper.toDTO(todoEntity)).thenReturn(todoResponseDTO);

        List<ToDoResponseDTO> result = toDoService.getAllToDosSortedByDeadline(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(todoId, result.getFirst().getId());
        verify(toDoRepository).findByUserId(userId, Sort.by("deadlineDatum").ascending()
                .and(Sort.by("priority").ascending())
                .and(Sort.by("title").ascending()));
    }
}
