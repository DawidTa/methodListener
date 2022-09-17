package pl.methodListener.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.methodListener.entity.LogRequestEntity;
import pl.methodListener.entity.UserEntity;
import pl.methodListener.model.UserModel;
import pl.methodListener.repository.LogRequestRepository;
import pl.methodListener.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogRequestAspectTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRequestRepository logRequestRepository;
    @Autowired
    private ObjectMapper objectMapper;

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
    public void checkLogRequestWithGetUser() throws Throwable {

        mockMvc.perform(get("/user/{login}", "testowy123"))
                .andDo(print())
                .andExpect(status().isOk());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getAttributes()).isEqualTo("[testowy123]");
        assertThat(loggedRequest.get().getBody()).isEqualTo("testowy123");
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("200 OK");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("getUser");
        assertThat(loggedRequest.get().getTime()).isNotZero();

    }

    @Test
    public void checkLogRequestWithPostUser() throws Throwable {

        UserModel user = new UserModel("Dawid", "Taczkowski", "test@test.pl", "testowy", "test123!");
        String jsonContent = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                        .content(jsonContent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getAttributes()).isEqualTo("[UserModel(name=Dawid, lastname=Taczkowski, email=test@test.pl, login=testowy, password=test123!)]");
        assertThat(loggedRequest.get().getBody()).isEqualTo("UserModel(name=Dawid, lastname=Taczkowski, email=test@test.pl, login=testowy, password=test123!)");
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("201 CREATED");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("addUser");
        assertThat(loggedRequest.get().getTime()).isNotZero();

    }

    @Test
    public void checkLogRequestWithPutUser() throws Throwable {

        UserModel userModel = new UserModel("Test123", "test123", "testaaa123@test.pl", "testl123", "test123!");

        String jsonContent = objectMapper.writeValueAsString(userModel);

        mockMvc.perform(put("/user/{login}", "testowy123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getAttributes()).isEqualTo("[testowy123, UserModel(name=Test123, lastname=test123, email=testaaa123@test.pl, login=testl123, password=test123!)]");
        assertThat(loggedRequest.get().getBody()).isEqualTo("UserModel(name=Test123, lastname=test123, email=testaaa123@test.pl, login=testl123, password=test123!)");
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("200 OK");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("updateUser");
        assertThat(loggedRequest.get().getTime()).isNotZero();

    }

    @Test
    @Transactional
    public void checkLogRequestWithDeleteUser() throws Throwable {

        mockMvc.perform(delete("/user/{login}", "testowy123"))
                .andDo(print())
                .andExpect(status().isOk());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getAttributes()).isEqualTo("[testowy123]");
        assertThat(loggedRequest.get().getBody()).isEqualTo("testowy123");
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("200 OK");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("deleteUser");
        assertThat(loggedRequest.get().getTime()).isNotZero();

    }

    @Test
    public void checkLogRequestUserNotFound() throws Exception {

        mockMvc.perform(get("/user/{login}", "testowy"))
                .andDo(print())
                .andExpect(status().isNotFound());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getAttributes()).isEqualTo("[pl.kurs.testdt5.helper.UserNotFoundException: Could not find user testowy]");
        assertThat(loggedRequest.get().getBody()).isEqualTo("pl.kurs.testdt5.helper.UserNotFoundException: Could not find user testowy");
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("404 NOT_FOUND");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("handleUserNotFoundException");
        assertThat(loggedRequest.get().getTime()).isNotZero();
    }

    @Test
    public void checkLogRequestMailOrLoginAlreadyExist() throws Exception {

        UserModel userModel = new UserModel("Dawid", "Testowy", "test123@test.pl", "testowy123", "test123!");

        String jsonContent = objectMapper.writeValueAsString(userModel);

        mockMvc.perform(put("/user/{login}", "testowy123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isBadRequest());

        List<LogRequestEntity> logRequest = logRequestRepository.findAll();
        Optional<LogRequestEntity> loggedRequest = logRequest.stream().findFirst();

        assertThat(loggedRequest).isNotEmpty();
        assertThat(loggedRequest.get().getCookies()).isEqualTo("null");
        assertThat(loggedRequest.get().getResponseCode()).isEqualTo("400 BAD_REQUEST");
        assertThat(loggedRequest.get().getMethod()).isEqualTo("handleMethodArgumentNotValidException");
        assertThat(loggedRequest.get().getTime()).isNotZero();
    }
}