package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService userService;

    private final TaskStatusRepository taskStatusRepository;

    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) {
        initUser();
        initTaskStatuses();
        intiLabels();
    }

    private void initUser() {
        User userData = new User();
        userData.setEmail("hexlet@example.com");
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        User guest = new User();
        guest.setEmail("guest@example.com");
        guest.setPasswordDigest("qwerty");
        userService.createUser(guest);
    }

    private void initTaskStatuses() {
        List<TaskStatus> taskStatuses = List.of(
                new TaskStatus(null, "Draft", "draft", null),
                new TaskStatus(null, "ToReview", "to_review", null),
                new TaskStatus(null, "ToBeFixed", "to_be_fixed", null),
                new TaskStatus(null, "ToPublish", "to_publish", null),
                new TaskStatus(null, "Published", "published", null)
        );
        taskStatusRepository.saveAll(taskStatuses);
    }

    private void intiLabels() {
        List<Label> labels = List.of(
                new Label(null, "bug", null),
                new Label(null, "feature", null)
        );
        labelRepository.saveAll(labels);
    }
}
