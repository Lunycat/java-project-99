package hexlet.code.contoller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskSpecification specification;

    private JwtRequestPostProcessor token;

    private TaskStatus testTaskStatus;

    private User testUser;

    private Task testTask;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        taskStatusRepository.save(testTaskStatus);
        userRepository.save(testUser);
        labelRepository.save(testLabel);

        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(Set.of(testLabel));

        taskRepository.save(testTask);
    }

    @Test
    public void indexTest() throws Exception {
        Specification<Task> spec = specification.build(new TaskParamsDTO());

        MockHttpServletResponse response = mockMvc.perform(
                get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        List<Task> expected = taskRepository.findAll(spec);
        List<Task> actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void showTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        Task expected = taskRepository.findById(testTask.getId()).get();
        Task actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void createTest() throws Exception {
        TaskCreateDTO data = new TaskCreateDTO();

        data.setTitle("Title");
        data.setAssigneeId(testUser.getId());
        data.setStatus(testTaskStatus.getSlug());
        data.setLabelsId(new HashSet<>(Set.of(testLabel.getId())));

        mockMvc.perform(post("/api/tasks")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated());

        Task task = taskRepository.findByName(data.getTitle()).get();

        assertEquals(data.getTitle(), task.getName());
        assertEquals(data.getStatus(), task.getTaskStatus().getSlug());
        assertEquals(data.getAssigneeId(), task.getAssignee().getId());
        Set<Label> labels = new HashSet<>(labelRepository.findAllById(data.getLabelsId()));
        assertEquals(task.getLabels(), labels);
    }

    @Test
    public void updateTest() throws Exception {
        TaskStatus taskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        User user = Instancio.of(modelGenerator.getUserModel()).create();
        Label label = Instancio.of(modelGenerator.getLabelModel()).create();

        taskStatusRepository.save(taskStatus);
        userRepository.save(user);
        labelRepository.save(label);

        Map<String, Object> data = Map.of(
                "title", "title",
                "content", "Lots of letters...",
                "assigneeId", user.getId(),
                "status", taskStatus.getSlug(),
                "labelsId", new HashSet<>(Set.of(label.getId())));

        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk());

        Task task = taskRepository.findById(testTask.getId()).get();
        Set<Label> labels = new HashSet<>(Set.of(labelRepository.findById(label.getId()).get()));

        assertEquals(data.get("title"), task.getName());
        assertEquals(data.get("content"), task.getDescription());
        assertEquals(data.get("status"), task.getTaskStatus().getSlug());
        assertEquals(data.get("assigneeId"), task.getAssignee().getId());
        assertEquals(labels, task.getLabels());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("");
        });
    }
}
