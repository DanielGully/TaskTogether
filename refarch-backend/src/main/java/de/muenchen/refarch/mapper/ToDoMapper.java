package de.muenchen.refarch.mapper;

import de.muenchen.refarch.entities.ToDoEntity;
import de.muenchen.refarch.entities.dto.ToDoRequestDTO;
import de.muenchen.refarch.entities.dto.ToDoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ToDoMapper {
    ToDoMapper INSTANCE = Mappers.getMapper(ToDoMapper.class);

    ToDoEntity toEntity(ToDoRequestDTO toDoRequestDTO);

    ToDoResponseDTO toDTO(ToDoEntity toDoEntity);
}
