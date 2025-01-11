package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private String name;
    private int index;
    private String description;
    private String taskStatus;
    private Long assignee_id;
    private LocalDate createdAt;
}
