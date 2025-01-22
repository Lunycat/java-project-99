package hexlet.code.controller.api;

import hexlet.code.dto.tasksstatus.TaskStatusCreateDTO;
import hexlet.code.dto.tasksstatus.TaskStatusDTO;
import hexlet.code.dto.tasksstatus.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusController {

    private final TaskStatusService service;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index() {
        List<TaskStatusDTO> taskStatusesDTO = service.findAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatusesDTO.size()))
                .body(taskStatusesDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@RequestBody @Valid TaskStatusCreateDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO put(@RequestBody @Valid TaskStatusUpdateDTO dto, @PathVariable Long id) {
        return service.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
