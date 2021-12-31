package com.example.annotation.validator;

import com.example.annotation.ValidImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private final String[] supportedTypes = new String[]{"image/jpeg", "image/png"};

    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String contentType = multipartFile.getContentType();
        return isSupportedContentType(contentType);
    }

    private boolean isSupportedContentType(String contentType) {
        for (String type : supportedTypes) {
            if (type.equalsIgnoreCase(contentType))
                return true;
        }
        return false;
    }
}