package pl.kurs.testdt5.validation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kurs.testdt5.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginExistValidator implements ConstraintValidator<LoginExist, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByLogin(login);
    }
}
