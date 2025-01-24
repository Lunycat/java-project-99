package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {

    @Size(min = 1)
    private String title;

    @NotNull
    private String status;

    private int index;
    private String content;
    private Long assigneeId;
    private Set<Long> taskLabelIds = new HashSet<>();
}
