package de.muenchen.refarch.controller;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import de.muenchen.refarch.services.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ToDoControllerTest {

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoController toDoController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(toDoController).build();
    }

    @Test
    public void testGetToDo() throws Exception {
        UUID todoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ToDoResponseDTO responseDTO = new ToDoResponseDTO();
        responseDTO.setId(todoId);
        when(toDoService.getToDo(todoId, userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/todos/{todoId}", todoId)
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId.toString()));
    }

    @Test
    public void testCreateToDo() throws Exception {
        ToDoRequestDTO requestDTO = new ToDoRequestDTO();
        requestDTO.setTitle("Test ToDo");
        requestDTO.setDescription("Test Description");
        requestDTO.setPriority(Priority.HOCH);
        UUID userId = UUID.randomUUID();
        ToDoResponseDTO responseDTO = new ToDoResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        when(toDoService.createToDo(any(ToDoRequestDTO.class), any(UUID.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test ToDo\",\"description\":\"Test Description\",\"priority\":\"HOCH\"}")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateToDo() throws Exception {
        UUID todoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ToDoRequestDTO requestDTO = new ToDoRequestDTO();
        requestDTO.setTitle("Updated Title");
        requestDTO.setDescription("Updated Description");
        ToDoResponseDTO responseDTO = new ToDoResponseDTO();
        responseDTO.setId(todoId);
        when(toDoService.updateToDo(any(ToDoRequestDTO.class), any(UUID.class), any(UUID.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/todos/{todoId}", todoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"description\":\"Updated Description\",\"priority\":\"HOCH\"}")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId.toString()));
    }

    @Test
    public void testDeleteToDo() throws Exception {
        UUID todoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/todos/{todoId}", todoId).param("userId", userId.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetToDosByPriority() throws Exception {
        Priority priority = Priority.HOCH;
        UUID userId = UUID.randomUUID();
        ToDoResponseDTO responseDTO = new ToDoResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        List<ToDoResponseDTO> responseDTOList = List.of(responseDTO);
        when(toDoService.getToDosByPriority(priority, userId)).thenReturn(responseDTOList);

        mockMvc.perform(get("/todos/priority")
                        .param("priority", priority.name())
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId().toString()));
    }

    @Test
    public void testGetAllToDosSortedByDeadline() throws Exception {
        UUID userId = UUID.randomUUID();
        ToDoResponseDTO responseDTO = new ToDoResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        List<ToDoResponseDTO> responseDTOList = List.of(responseDTO);
        when(toDoService.getAllToDosSortedByDeadline(userId)).thenReturn(responseDTOList);

        mockMvc.perform(get("/todos/sorted")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId().toString()));
    }
}
