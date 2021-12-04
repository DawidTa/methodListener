package pl.kurs.testdt5.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.testdt5.aop.LogRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHelper {

    @LogRequest
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();
        List<String> collect = allErrors.stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ResponseEntity<>(collect, HttpStatus.BAD_REQUEST);
    }
}
