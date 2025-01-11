package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<Integer> index;
    private JsonNullable<String> title;
    private JsonNullable<String>  text;
    private JsonNullable<String>  content;
    private JsonNullable<String>  status;
    private JsonNullable<Long> assigneeId;
}
