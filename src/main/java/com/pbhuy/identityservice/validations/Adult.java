package com.pbhuy.identityservice.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AdultValidator.class})
public @interface Adult {
    String message() default "Must be an adult";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min();
}
