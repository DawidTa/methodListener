package pl.kurs.testdt5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.model.UserModelId;
import pl.kurs.testdt5.repository.UserRepository;
import pl.kurs.testdt5.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserEntity userEntity;
    @MockBean
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        this.userEntity = initializeUser();
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

        //when(userServiceMock.getUserRest(userModelId)).thenReturn(null);

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
        userModelId.setId(2);
        String jsonContent = objectMapper.writeValueAsString(userModelId);

        userEntity = initializeUser();

        when(userServiceMock.getUserRest(any(UserModelId.class))).thenReturn(userEntity);

        mockMvc.perform(get("/user/get")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Dawid"))
                .andExpect(jsonPath("$.lastname").value("Taczkowski"))
                .andExpect(jsonPath("$.email").value("teasefghjt@test.pl"))
                .andExpect(jsonPath("$.login").value("tesasdfghjogin"))
                .andExpect(jsonPath("$.password").value("test123!"));
    }

    @Test
    void addUserShouldReturnBadRequest() throws Exception {
        UserModel user = null;
        String jsonContent = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/add")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser() throws Exception {
        UserModel user = new UserModel("Dawid", "Taczkowski", "test123@test.pl", "testowy123", "test123!");
        String jsonContent = objectMapper.writeValueAsString(user);

        when(userServiceMock.addUserRest(any(UserModel.class))).thenReturn(userEntity);

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
        UserModelId userModel = new UserModelId("Dave", "Taczkowski", "testaaa@test.pl", "testl", "test123!", 1);

        String jsonContent = objectMapper.writeValueAsString(userModel);

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
        userModelId.setId(2);

        String jsonContent = objectMapper.writeValueAsString(userModelId);

        mockMvc.perform(delete("/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private UserEntity initializeUser() {
        return new UserEntity(2, "Dawid", "Taczkowski", "teasefghjt@test.pl", "tesasdfghjogin", "test123!");
    }
}