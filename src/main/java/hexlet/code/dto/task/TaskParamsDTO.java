package hexlet.code.dto.task;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskParamsDTO {

    private String titleCont;
    private Long assigneeId;
    private String status;
    private Long taskLabelIds;
}
