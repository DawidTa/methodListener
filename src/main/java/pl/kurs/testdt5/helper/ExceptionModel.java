package pl.kurs.testdt5.helper;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExceptionModel {
    private String message;
    private String exceptionType;
    private String status;
}
