package pl.kurs.testdt5.validation;

import lombok.RequiredArgsConstructor;
import pl.kurs.testdt5.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class LoginExistValidator implements ConstraintValidator<LoginExist, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByLogin(login);
    }
}
