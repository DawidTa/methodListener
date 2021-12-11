package pl.kurs.testdt5.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.model.UserModelId;
import pl.kurs.testdt5.repository.UserRepository;
import pl.kurs.testdt5.service.UserService;

import java.net.http.HttpClient;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private UserEntity userEntity;
    @MockBean
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        userEntity = initializeUser();
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void ensureThatJsonIsReturned() {

    }

    @Test
    public void givenIdDoesNotExist() throws Exception {

        UserModelId userModelId = new UserModelId();
        userModelId.setId(15);

        String jsonContent = objectMapper.writeValueAsString(userModelId);

        when(userServiceMock.getUserRest(userModelId)).thenReturn(null);

        mockMvc.perform(get("/user/get")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUser() throws Exception {

        UserModelId userModelId = new UserModelId();
        userModelId.setId(1);

        String jsonContent = objectMapper.writeValueAsString(userModelId);

        when(userServiceMock.getUserRest(userModelId)).thenReturn(userEntity);

        mockMvc.perform(get("/user/get")
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addUser() throws Exception {
        UserEntity user = new UserEntity( "Dawid", "Taczkowski", "test123@test.pl", "testowy123", "test123!");
        //userRepository.save(user);

        String jsonContent = objectMapper.writeValueAsString(user);

//        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(post("/user/add")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Dawid"))
                .andExpect(jsonPath("$.lastname").value("Taczkowski"))
                .andExpect(jsonPath("$.email").value("test123@test.pl"))
                .andExpect(jsonPath("$.login").value("testowy123"))
                .andExpect(jsonPath("$.password").value("test123!"));
    }

    @Test
    void updateUser() throws Exception {
        UserEntity user = new UserEntity(1, "Dawid", "Taczkowski", "test@test.pl", "testlogin", "test123!");
        UserModel userModel = new UserModel("Dave", "Taczkowski", "test@test.pl", "testl", "test123!");

        String jsonContent = objectMapper.writeValueAsString(userModel);

        user.setName(userModel.getName());
        userModel.setLogin(userModel.getLogin());

        mockMvc.perform(put("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonContent));
    }

    @Test
    void deleteUser() throws Exception {
        UserModelId userModelId = new UserModelId();
        userModelId.setId(1);

        String jsonContent = objectMapper.writeValueAsString(userModelId);

        mockMvc.perform(delete("/user/delete/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private UserEntity initializeUser() {
        return new UserEntity(1, "Dawid", "Taczkowski", "teasefghjt@test.pl", "tesasdfghjogin", "test123!");
    }
}