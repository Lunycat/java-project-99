package hexlet.code.dto.tasksstatus;

import jakarta.validation.constraints.Min;

public class TaskStatusCreateDTO {

    @Min(value = 1)
    private String name;

    @Min(value = 1)
    private String slug;
}
