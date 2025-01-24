package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {

    @Size(min = 1)
    private JsonNullable<String> title;

    @NotNull
    private JsonNullable<String> status;

    private JsonNullable<Integer> index;
    private JsonNullable<String> text;
    private JsonNullable<String> content;
    private JsonNullable<Long> assigneeId;
    private JsonNullable<Set<Long>> taskLabelIds;
}
