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
import pl.kurs.testdt5.helper.UserNotFoundException;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        userEntity.setId("f3d106f3-baf5-4964-844f-f1980d2eec90")
                .setName("Dave")
                .setLastname("Test")
                .setEmail("test123@test.pl")
                .setLogin("testowy123")
                .setPassword("test123!");
        userRepository.save(userEntity);
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/somePath")).andDo(print()).andExpect(status().isNotFound());
    }


    @Test
    public void givenLoginDoesNotExist() throws Exception {

        mockMvc.perform(get("/user/{login}", "testowylogin"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find user testowylogin"))
                .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"))
                .andExpect(jsonPath("$.status").value("404 NOT_FOUND"));
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
                .andExpect(status().isCreated()).andReturn();

        UserEntity userEntity = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getName()).isEqualTo("Dawid");
        assertThat(userEntity.getLastname()).isEqualTo("Taczkowski");
        assertThat(userEntity.getEmail()).isEqualTo("test@test.pl");
        assertThat(userEntity.getLogin()).isEqualTo("testowy");
        assertThat(userEntity.getPassword()).isEqualTo("test123!");

    }

    @Test
    public void updateUser() throws Exception {

        UserModel userModel = new UserModel("Test123", "test123", "testaaa123@test.pl", "testl123", "test123!");
        String userId = userRepository.findByLogin("testowy123").orElseThrow(() -> new UserNotFoundException("testowy123")).getId();

        String jsonContent = objectMapper.writeValueAsString(userModel);

        MvcResult result = mockMvc.perform(put("/user/{login}", "testowy123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserEntity userEntity = objectMapper.readValue(result.getResponse().getContentAsString(), UserEntity.class);
        assertThat(userEntity.getId()).isEqualTo(userId);
        assertThat(userEntity.getName()).isEqualTo("Test123");
        assertThat(userEntity.getLastname()).isEqualTo("test123");
        assertThat(userEntity.getEmail()).isEqualTo("testaaa123@test.pl");
        assertThat(userEntity.getLogin()).isEqualTo("testl123");
        assertThat(userEntity.getPassword()).isEqualTo("test123!");

    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/user/{login}", "testowy123"))
                .andDo(print())
                .andExpect(status().isOk());

        assertThat(userRepository.findByLogin("testowy123")).isEmpty();
    }
}