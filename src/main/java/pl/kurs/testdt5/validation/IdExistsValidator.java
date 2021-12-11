package pl.kurs.testdt5.validation;

import lombok.RequiredArgsConstructor;
import pl.kurs.testdt5.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class IdExistsValidator implements ConstraintValidator<IdExists, Integer> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.existsById(id);
    }
}
