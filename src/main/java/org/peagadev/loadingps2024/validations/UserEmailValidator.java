package org.peagadev.loadingps2024.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.repository.UserRepository;

import java.lang.annotation.Annotation;
@RequiredArgsConstructor
public class UserEmailValidator implements ConstraintValidator<UserEmailValidation,String> {

    private final UserRepository userRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findByEmail(email).isEmpty();
    }
}
