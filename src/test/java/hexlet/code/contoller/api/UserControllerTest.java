package hexlet.code.contoller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    private JwtRequestPostProcessor token;

    private User testUser;
    private StatusResultMatchers status;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
        userRepository.save(testUser);
    }

    @Test
    public void indexTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        List<User> expected = userRepository.findAll();
        List<User> actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void showTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        String body = response.getContentAsString();

        User expected = userRepository.findById(testUser.getId()).get();
        User actual = om.readValue(body, new TypeReference<>() { });

        assertEquals(expected, actual);
    }

    @Test
    public void createTest() throws Exception {
        User data = Instancio.of(modelGenerator.getUserModel()).create();

        mockMvc.perform(post("/api/users")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isCreated());

        User user = userRepository.findByEmail(data.getEmail()).get();

        assertEquals(data.getEmail(), user.getEmail());
        assertEquals(data.getFirstName(), user.getFirstName());
        assertEquals(data.getLastName(), user.getLastName());
    }

    @Test
    public void updateCorrectTokenTest() throws Exception {
        Map<String, String> data = Map.of(
                "firstName", "Mike",
                "lastName", "Smith",
                "password", "qwerty");

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk());

        User user = userRepository.findById(testUser.getId()).get();

        assertEquals(data.get("firstName"), user.getFirstName());
        assertEquals(data.get("lastName"), user.getLastName());
        assertTrue(user.getPassword().length() > 20);
    }

    @Test
    public void updateInCorrectTokenTest() throws Exception {
        Map<String, String> data = Map.of(
                "firstName", "Mike",
                "lastName", "Smith",
                "password", "qwerty");

        JwtRequestPostProcessor inCorrectToken = jwt().jwt(builder -> builder.subject("dsadasf@mail.ru"));

        status = status();
        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .with(inCorrectToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status.isForbidden());
    }

    @Test
    public void deleteCorrectTokenTest() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());

        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    public void deleteInCorrectTokenTest() throws Exception {
        JwtRequestPostProcessor inCorrectToken = jwt().jwt(builder -> builder.subject("dsadasf@mail.ru"));

        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(inCorrectToken))
                .andExpect(status().isForbidden());

        assertTrue(userRepository.existsById(testUser.getId()));
    }
}
