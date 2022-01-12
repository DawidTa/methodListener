package pl.kurs.testdt5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.testdt5.aop.LogRequest;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.repository.UserRepository;
import pl.kurs.testdt5.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @LogRequest
    @GetMapping()
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
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
        userRepository.deleteByLogin(login);
        return ResponseEntity.ok("User deleted");
    }
}
