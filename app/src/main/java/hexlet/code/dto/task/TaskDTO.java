package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {

    private Long id;
    private int index;
    private String title;
    private String content;
    private String status;
    private Long assigneeId;
    private LocalDate createdAt;
}
