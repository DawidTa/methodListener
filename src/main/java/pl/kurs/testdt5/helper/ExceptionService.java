package pl.kurs.testdt5.helper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
}
