package de.muenchen.refarch.controller;

import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.refarch.TestConstants;
import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.repositories.ToDoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.UUID;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class ToDoControllerIntegrationTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ToDoRepository toDoRepository;

    private final UUID userId = UUID.randomUUID();
    private UUID todoId;

    @BeforeEach
    void setUp() {
        ToDoEntity todoEntity = new ToDoEntity();
        todoEntity.setTitle("Test ToDo");
        todoEntity.setDescription("Test description");
        todoEntity.setPriority(Priority.MITTEL);
        todoEntity.setDeadlineDatum(LocalDate.now().plusDays(5));
        todoEntity.setUserId(userId);
        todoEntity = toDoRepository.save(todoEntity);
        todoId = todoEntity.getId();

        for (int i = 0; i < 3; i++) {
            ToDoEntity additionalToDo = new ToDoEntity();
            additionalToDo.setTitle("Additional ToDo " + i);
            additionalToDo.setDescription("Description " + i);
            additionalToDo.setPriority(i % 2 == 0 ? Priority.MITTEL : Priority.NIEDRIG);
            additionalToDo.setDeadlineDatum(LocalDate.now().plusDays(i + 1));
            additionalToDo.setUserId(userId);
            toDoRepository.save(additionalToDo);
        }
    }

    @AfterEach
    void tearDown() {
        toDoRepository.deleteAll();
    }

    @Nested
    class GetToDo {
        @Test
        void givenTodoId_thenReturnToDo() throws Exception {
            mockMvc.perform(get("/todos/{todoId}", todoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(todoId.toString())))
                    .andExpect(jsonPath("$.title", is("Test ToDo")));
        }

        @Test
        void givenInvalidTodoId_thenReturnNotFound() throws Exception {
            UUID invalidTodoId = UUID.randomUUID();
            mockMvc.perform(get("/todos/{todoId}", invalidTodoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateToDo {
        @Test
        void givenToDoRequest_thenCreateToDo() throws Exception {
            ToDoRequestDTO requestDTO = new ToDoRequestDTO();
            requestDTO.setTitle("New ToDo");
            requestDTO.setDescription("New description");
            requestDTO.setPriority(Priority.HOCH);
            requestDTO.setDeadlineDatum(LocalDate.now().plusDays(10));

            mockMvc.perform(post("/todos")
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title", is("New ToDo")));
        }

        @Test
        void givenInvalidToDoRequest_thenReturnBadRequest() throws Exception {
            ToDoRequestDTO requestDTO = new ToDoRequestDTO();
            requestDTO.setDescription("Description without title");
            requestDTO.setPriority(Priority.HOCH);
            requestDTO.setDeadlineDatum(LocalDate.now().plusDays(10));

            mockMvc.perform(post("/todos")
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateToDo {
        @Test
        void givenTodoIdAndUpdateRequest_thenUpdateToDo() throws Exception {
            ToDoRequestDTO requestDTO = new ToDoRequestDTO();
            requestDTO.setTitle("Updated ToDo");
            requestDTO.setDescription("Updated description");
            requestDTO.setPriority(Priority.NIEDRIG);
            requestDTO.setDeadlineDatum(LocalDate.now().plusDays(2));

            mockMvc.perform(put("/todos/{todoId}", todoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(todoId.toString())))
                    .andExpect(jsonPath("$.title", is("Updated ToDo")));
        }

        @Test
        void givenInvalidTodoIdAndUpdateRequest_thenReturnNotFound() throws Exception {
            UUID invalidTodoId = UUID.randomUUID();
            ToDoRequestDTO requestDTO = new ToDoRequestDTO();
            requestDTO.setTitle("Updated ToDo");
            requestDTO.setDescription("Updated description");
            requestDTO.setPriority(Priority.NIEDRIG);
            requestDTO.setDeadlineDatum(LocalDate.now().plusDays(2));

            mockMvc.perform(put("/todos/{todoId}", invalidTodoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteToDo {
        @Test
        void givenTodoId_thenDeleteToDo() throws Exception {
            mockMvc.perform(delete("/todos/{todoId}", todoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        void givenInvalidTodoId_thenReturnNotFound() throws Exception {
            UUID invalidTodoId = UUID.randomUUID();
            mockMvc.perform(delete("/todos/{todoId}", invalidTodoId)
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetToDosByPriority {
        @Test
        void givenPriority_thenReturnTodos() throws Exception {
            mockMvc.perform(get("/todos/priority")
                            .param("priority", "MITTEL")
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                    .andExpect(jsonPath("$[0].priority", is("MITTEL")));
        }

        @Test
        void givenInvalidPriority_thenReturnBadRequest() throws Exception {
            mockMvc.perform(get("/todos/priority")
                            .param("priority", "INVALID_PRIORITY")
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetAllToDosSortedByDeadline {
        @Test
        void givenUserId_thenReturnSortedTodos() throws Exception {
            mockMvc.perform(get("/todos/sorted")
                            .param("userId", userId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                    .andExpect(jsonPath("$[0].title", is("Additional ToDo 0")))
                    .andExpect(jsonPath("$[-1].title", is("Test ToDo")));
        }
        @Test
        void givenUserIdWithNoTodos_thenReturnEmptyList() throws Exception {
            UUID userIdWithNoTodos = UUID.randomUUID();

            mockMvc.perform(get("/todos/sorted")
                            .param("userId", userIdWithNoTodos.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}
