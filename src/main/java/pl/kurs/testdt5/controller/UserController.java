package pl.kurs.testdt5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.testdt5.aop.LogRequest;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.model.UserModelId;
import pl.kurs.testdt5.repository.UserRepository;
import pl.kurs.testdt5.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    private final UserRepository userRepository;

    @LogRequest
    @GetMapping("/get")
    public ResponseEntity getUser(@RequestBody @Valid UserModelId userModelId) {
        return ResponseEntity.ok(userService.getUserRest(userModelId));
    }

    @LogRequest
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid UserModel model) {
        userService.addUserRest(model);
        return new ResponseEntity(model, HttpStatus.CREATED);
    }

//    @LogRequest
//    @PutMapping("/update")
//    public ResponseEntity updateUser(@RequestBody @Valid UserModelId userModelId) {
//        return ResponseEntity.ok(userService.updateUserRest(userModelId));
//    }

    @LogRequest
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(@RequestBody @Valid UserModelId userModelId) {
        userRepository.deleteById(userModelId.getId());
        return ResponseEntity.ok("User deleted");
    }
}
