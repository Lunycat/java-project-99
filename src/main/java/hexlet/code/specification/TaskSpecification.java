package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCon(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatus(params.getStatus()))
                .and(withLabelId(params.getTaskLabelIds()));
    }

    private Specification<Task> withTitleCon(String title) {
        return (root, query, cb) -> title == null
                    ? cb.conjunction()
                    : cb.like(root.get("name"), "%" + title + "%");
    }

    private Specification<Task> withAssigneeId(Long id) {
        return (root, query, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), id);
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.like(root.get("taskStatus").get("slug"), "%" + status + "%");
    }

    private Specification<Task> withLabelId(Long id) {
        return (root, query, cb) -> id == null
                ? cb.conjunction()
                : cb.equal(root.get("labels").get("id"), id);
    }
}
