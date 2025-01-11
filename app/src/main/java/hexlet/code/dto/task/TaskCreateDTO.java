package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    private int index;
    private String title;
    private String text;
    private String content;
    private String status;
    private Long assigneeId;
}
