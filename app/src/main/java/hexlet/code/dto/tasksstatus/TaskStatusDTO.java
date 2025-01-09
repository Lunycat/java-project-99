package hexlet.code.dto.tasksstatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusDTO {

    private Long id;
    private String name;
    private String slug;
}
