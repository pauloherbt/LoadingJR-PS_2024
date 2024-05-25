package org.peagadev.loadingps2024.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

public class ImageExtensionValidator implements ConstraintValidator<ImageExtensionValidation, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(ImageExtensionValidation constraintAnnotation) {
        allowedExtensions = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file!=null){
            var teste = file.getContentType().contains("image");
            return teste;
        }
        return true;
    }
}
