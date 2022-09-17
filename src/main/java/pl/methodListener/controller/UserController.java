package pl.methodListener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.methodListener.aop.LogRequest;
import pl.methodListener.entity.UserEntity;
import pl.methodListener.model.UserModel;
import pl.methodListener.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @LogRequest
    @GetMapping()
    public ResponseEntity getAllUsers() {
        List<UserEntity> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @LogRequest
    @GetMapping(value = "/{login}")
    public ResponseEntity getUser(@PathVariable("login") String login) {
        return ResponseEntity.ok(userService.getUserRest(login));
    }

    @LogRequest
    @PostMapping
    public ResponseEntity addUser(@RequestBody @Valid UserModel model) {
        return new ResponseEntity(userService.addUserRest(model), HttpStatus.CREATED);
    }

    @LogRequest
    @PutMapping("/{login}")
    public ResponseEntity updateUser(@Valid @PathVariable("login") String login, @RequestBody @Valid UserModel userModel) {
        return ResponseEntity.ok(userService.updateUserRest(login, userModel));
    }

    @LogRequest
    @DeleteMapping("/{login}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable("login") String login) {
        userService.deleteUser(login);
        return ResponseEntity.ok("User deleted");
    }
}
