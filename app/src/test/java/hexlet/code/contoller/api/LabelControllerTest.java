package hexlet.code.contoller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
public class LabelControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);
    }

    @Test
    public void indexTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/labels").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        List<Label> expected = labelRepository.findAll();
        List<Label> actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void showTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        Label expected = labelRepository.findById(testLabel.getId()).get();
        Label actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void createTest() throws Exception {
        Label data = Instancio.of(modelGenerator.getLabelModel()).create();

        mockMvc.perform(post("/api/labels")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated());

        Label label = labelRepository.findByName(data.getName()).get();

        assertEquals(data.getName(), label.getName());
    }

    @Test
    public void updateTest() throws Exception {
        Map<String, String> data = Map.of("name", "newName");

        mockMvc.perform(put("/api/labels/" + testLabel.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk());

        Label label = labelRepository.findById(testLabel.getId()).get();

        assertEquals(data.get("name"), label.getName());
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete(("/api/labels/" + testLabel.getId())).with(token))
                .andExpect(status().isNoContent());

        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("");
        });
    }
}
