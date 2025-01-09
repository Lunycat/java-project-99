package hexlet.code.contoller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
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
import java.util.HashMap;
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
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserMapper userMapper;

    private JwtRequestPostProcessor token;

    private User testUser;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
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
        List<UserDTO> listDTOs = om.readValue(body, new TypeReference<>(){ });
        List<User> actual = userMapper.toListUser(listDTOs);

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
    public void updateTest() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.putAll(Map.of(
                "firstName", "Mike",
                "lastName", "Smith"));

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(data)))
                .andExpect(status().isOk());

        User user = userRepository.findById(testUser.getId()).get();

        assertEquals(data.get("firstName"), user.getFirstName());
        assertEquals(data.get("lastName"), user.getLastName());
    }

    @Test
    public void destroyTest() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isNoContent());

        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("");
        });
    }
}
