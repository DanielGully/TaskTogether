package de.muenchen.refarch.mapper;

import de.muenchen.refarch.entities.Priority;
import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ToDoMapperTest {

    private final ToDoMapper mapper = ToDoMapper.INSTANCE;

    @Test
    public void testToEntity() {
        ToDoRequestDTO requestDTO = new ToDoRequestDTO();
        requestDTO.setTitle("Test Title");
        requestDTO.setDescription("Test Description");
        requestDTO.setPriority(Priority.HOCH);
        requestDTO.setDeadlineDatum(LocalDate.now());
        requestDTO.setUserId(UUID.randomUUID());

        ToDoEntity entity = mapper.toEntity(requestDTO);

        assertEquals(requestDTO.getTitle(), entity.getTitle());
        assertEquals(requestDTO.getDescription(), entity.getDescription());
        assertEquals(requestDTO.getPriority(), entity.getPriority());
        assertEquals(requestDTO.getDeadlineDatum(), entity.getDeadlineDatum());
        assertEquals(requestDTO.getUserId(), entity.getUserId());
    }

    @Test
    public void testToEntity_NullInput() {
        ToDoEntity entity = mapper.toEntity(null);
        assertNull(entity, "Expected null when input is null");
    }

    @Test
    public void testToDTO() {
        ToDoEntity entity = new ToDoEntity();
        entity.setId(UUID.randomUUID());
        entity.setTitle("Test Title");
        entity.setDescription("Test Description");
        entity.setPriority(Priority.HOCH);
        entity.setDeadlineDatum(LocalDate.now());
        entity.setUserId(UUID.randomUUID());

        ToDoResponseDTO responseDTO = mapper.toDTO(entity);

        assertEquals(entity.getId(), responseDTO.getId());
        assertEquals(entity.getTitle(), responseDTO.getTitle());
        assertEquals(entity.getDescription(), responseDTO.getDescription());
        assertEquals(entity.getPriority(), responseDTO.getPriority());
        assertEquals(entity.getDeadlineDatum(), responseDTO.getDeadlineDatum());
    }

    @Test
    public void testToDTO_NullInput() {
        ToDoResponseDTO responseDTO = mapper.toDTO(null);
        assertNull(responseDTO, "Expected null when input is null");
    }
}
