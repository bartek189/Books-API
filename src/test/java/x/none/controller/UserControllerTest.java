package x.none.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import x.none.model.User;
import x.none.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    @BeforeEach
    public void init() {
        userRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void shouldSaveUser() {
        User user = new User("A", "B");

        String json = objectMapper.writeValueAsString(user);

        String response = mvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        User result = objectMapper.readValue(response, User.class);
        boolean a = encoder.matches("B", result.getPassword());


        assertEquals(1, userRepository.findAll().size());
        assertEquals("A", result.getUserName());
        assertTrue(a);
    }


}