package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    private String name;
    private int index;
    private String description;
    private String taskStatus;
    private Long assigneeId;
}
