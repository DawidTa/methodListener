package pl.kurs.testdt5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void initTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1")
                .setName("Dave")
                .setLastname("Test")
                .setEmail("test123@test.pl")
                .setLogin("testowy123")
                .setPassword("test123!");
        userRepository.save(userEntity);
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    public void givenIdDoesNotExist() throws Exception {

        mockMvc.perform(get("/user/{login}", "testowylogin"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUser() throws Exception {

        mockMvc.perform(get("/user/{login}", "testowy123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Dave"))
                .andExpect(jsonPath("$.lastname").value("Test"))
                .andExpect(jsonPath("$.email").value("test123@test.pl"))
                .andExpect(jsonPath("$.login").value("testowy123"))
                .andExpect(jsonPath("$.password").value("test123!"));
    }

    @Test
    public void addUserShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/user"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUser() throws Exception {
        UserModel user = new UserModel("Dawid", "Taczkowski", "test@test.pl", "testowy", "test123!");
        String jsonContent = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post("/user")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value("Dawid"))
                .andExpect(jsonPath("$.lastname").value("Taczkowski"))
                .andExpect(jsonPath("$.email").value("test@test.pl"))
                .andExpect(jsonPath("$.login").value("testowy"))
                .andExpect(jsonPath("$.password").value("test123!")).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void updateUser() throws Exception {

        UserModel userModel = new UserModel("Test123", "test123", "testaaa123@test.pl", "testl123", "test123!");

        String jsonContent = objectMapper.writeValueAsString(userModel);

        mockMvc.perform(put("/user/{login}", "testowy123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test123"))
                .andExpect(jsonPath("$.lastname").value("test123"))
                .andExpect(jsonPath("$.email").value("testaaa123@test.pl"))
                .andExpect(jsonPath("$.login").value("testl123"))
                .andExpect(jsonPath("$.password").value("test123!"));
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/user/{login}", "testowy123"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}