package pl.methodListener.validation;

import lombok.RequiredArgsConstructor;
import pl.methodListener.repository.UserRepository;

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
