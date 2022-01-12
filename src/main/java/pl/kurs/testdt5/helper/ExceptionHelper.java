package pl.kurs.testdt5.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.testdt5.aop.LogRequest;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHelper {

    private final ExceptionService exceptionService;

    @LogRequest
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();
        boolean field = allErrors.stream()
                .findAny().map(fieldError -> fieldError.getField().equals("id")).orElse(false);
        List<String> collect = allErrors.stream()
                .map(error -> "Error at field: " + error.getField() + "-" + " " + error.getDefaultMessage())
                .collect(Collectors.toList());
        if (field) {
            return new ResponseEntity<>(collect, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(collect, HttpStatus.BAD_REQUEST);
    }

    @LogRequest
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ExceptionModel model = exceptionService.setExceptionResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
    }

    @LogRequest
    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity handleNullPointerException(NullPointerException ex) {
        ExceptionModel model = exceptionService.setExceptionResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(model, HttpStatus.BAD_REQUEST);
    }

    @LogRequest
    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    public ResponseEntity handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        ExceptionModel model = exceptionService.setExceptionResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(model, HttpStatus.BAD_REQUEST);
    }

    @LogRequest
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ex) {
        ExceptionModel model = exceptionService.setExceptionResponse(ex, HttpStatus.NOT_FOUND);
        return new ResponseEntity(model, HttpStatus.NOT_FOUND);
    }
}
