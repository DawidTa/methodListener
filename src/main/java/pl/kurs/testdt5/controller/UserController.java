package pl.kurs.testdt5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity getUser(@RequestParam(required = false, value = "id") int id) {
        return ResponseEntity.ok(userService.getUserRest(id));
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid UserModel model) {
        userService.addUserRest(model);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@RequestBody UserModel model, @PathVariable int id) {
        return ResponseEntity.ok(userService.updateUserRest(id, model));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUserRest(id));
    }
}
