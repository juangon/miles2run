package org.milestogo.bean_validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.hibernate.validator.constraints.URL;

@URL
@Constraint(validatedBy = { ImageUrlValidator.class })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageUrl {

    String message() default "ImageUrl should end with .png or .jpg or .jpeg";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
