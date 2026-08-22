package com.sanjay.store.Users;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= Starting_Letter.class)
public @interface Starting {

    String message() default "Name must be start with Captial Letter";

    Class <?> []groups() default {};
    Class<? extends Payload>[] payload() default {};
}
