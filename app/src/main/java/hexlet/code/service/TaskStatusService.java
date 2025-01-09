package hexlet.code.service;

import hexlet.code.dto.tasksstatus.TaskStatusCreateDTO;
import hexlet.code.dto.tasksstatus.TaskStatusDTO;
import hexlet.code.dto.tasksstatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper mapper;

    public List<TaskStatusDTO> findAll() {
        List<TaskStatus> taskStatuses = repository.findAll();
        return mapper.toListTaskStatusDTO(taskStatuses);
    }

    public TaskStatusDTO findById(Long id) {
        TaskStatus taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        return mapper.toTaskStatusDTO(taskStatus);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        TaskStatus taskStatus = mapper.toTask(dto);
        repository.save(taskStatus);
        return mapper.toTaskStatusDTO(taskStatus);
    }

    public TaskStatusDTO put(TaskStatusUpdateDTO dto, Long id) {
        TaskStatus taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id));
        mapper.update(dto, taskStatus);
        repository.save(taskStatus);
        return mapper.toTaskStatusDTO(taskStatus);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
