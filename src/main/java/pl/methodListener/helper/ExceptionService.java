package pl.methodListener.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExceptionService {

    private ExceptionModel exceptionModel;

    public ExceptionService() {
        exceptionModel = new ExceptionModel();
    }

    public ExceptionModel setExceptionResponse(Exception ex, HttpStatus status) {

        return exceptionModel.setMessage(ex.getMessage())
                .setExceptionType(ex.getClass().getSimpleName())
                .setStatus(status.toString());
    }

    public ExceptionModel setExceptionResponseNotValidException(MethodArgumentNotValidException ex, HttpStatus status) {
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();

        List<String> collect = allErrors.stream()
                .map(error -> "Error at field: " + error.getField() + "-" + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return exceptionModel.setMessage(collect.toString())
                .setExceptionType(ex.getClass().getSimpleName())
                .setStatus(status.toString());
    }
}
