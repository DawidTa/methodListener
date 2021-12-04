package pl.kurs.testdt5.controller;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.kurs.testdt5.aop.LogRequest;
import pl.kurs.testdt5.model.UserModel;
import pl.kurs.testdt5.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @LogRequest
    @GetMapping("/get")
    public ResponseEntity getUser(@RequestParam(required = false, value = "id") Integer id) {
        return ResponseEntity.ok(userService.getUserRest(id));
    }

    @LogRequest
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody @Valid UserModel model) {
        userService.addUserRest(model);
        return new ResponseEntity(Map.of("User created", model), HttpStatus.CREATED);
    }

    @LogRequest
    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable int id, @RequestBody @Valid UserModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
            List<String> errorsMessage = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.ok(userService.updateUserRest(id, model));
    }

    @LogRequest
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUserRest(id));
    }
}
