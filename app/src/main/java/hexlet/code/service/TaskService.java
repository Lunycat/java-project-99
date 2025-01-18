package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private TaskSpecification specification;

    public Page<TaskDTO> findAll(TaskParamsDTO params, int page) {
        var spec = specification.build(params);
        Page<Task> tasks = repository.findAll(spec, PageRequest.of(page - 1, 10));
        return mapper.toPageTaskDTO(tasks);
    }

    public TaskDTO findById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found task with id = " + id));
        return mapper.toTaskDTO(task);
    }

    public TaskDTO save(TaskCreateDTO dto) {
        Task task = mapper.toTask(dto);
        repository.save(task);
        return mapper.toTaskDTO(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found task with id = " + id));
        mapper.update(dto, task);
        repository.save(task);
        return mapper.toTaskDTO(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
