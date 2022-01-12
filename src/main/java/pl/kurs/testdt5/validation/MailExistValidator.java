package pl.kurs.testdt5.validation;

import lombok.RequiredArgsConstructor;
import pl.kurs.testdt5.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class MailExistValidator implements ConstraintValidator<MailExist, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(email);
    }
}
