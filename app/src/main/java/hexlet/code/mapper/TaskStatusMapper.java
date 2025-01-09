package hexlet.code.mapper;

import hexlet.code.dto.tasksstatus.TaskStatusCreateDTO;
import hexlet.code.dto.tasksstatus.TaskStatusDTO;
import hexlet.code.dto.tasksstatus.TaskStatusUpdateDTO;

import hexlet.code.model.TaskStatus;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    public abstract TaskStatusDTO toTaskStatusDTO(TaskStatus model);
    public abstract TaskStatus toTask(TaskStatusCreateDTO dto);
    public abstract void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus model);
    public abstract List<TaskStatusDTO> toListTaskStatusDTO(List<TaskStatus> models);
}
