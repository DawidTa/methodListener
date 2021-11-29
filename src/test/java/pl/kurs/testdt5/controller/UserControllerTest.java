package pl.kurs.testdt5.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.testdt5.entity.LogRequestEntity;
import pl.kurs.testdt5.entity.UserEntity;
import pl.kurs.testdt5.service.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userServiceMock;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void getUser() throws Exception {
        UserEntity user = new UserEntity(1,"Dawid","Taczkowski","test@test.pl", "testlogin", "test123!");
        when(userServiceMock.getUserRest(1)).thenReturn(user);

        mockMvc.perform(get("/user/get")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addUser() throws Exception {
        UserEntity user = new UserEntity(1,"Dawid","Taczkowski","test@test.pl", "testlogin", "test123!");

        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateUser() throws Exception {

    }

    @Test
    void deleteUser() throws Exception {
        when(userServiceMock.deleteUserRest(1)).thenReturn(status().isOk());
        mockMvc.perform(delete("/user/delete/", 1))
                .andExpect(status().isOk());
    }

    private String toJson(UserEntity user) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(user);
    }
}