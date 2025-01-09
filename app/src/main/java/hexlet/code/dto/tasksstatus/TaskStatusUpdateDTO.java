package hexlet.code.dto.tasksstatus;

import jakarta.validation.constraints.Min;
import org.openapitools.jackson.nullable.JsonNullable;

public class TaskStatusUpdateDTO {

    @Min(value = 1)
    private JsonNullable<String> name;

    @Min(value = 1)
    private JsonNullable<String> slug;
}
