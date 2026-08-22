package com.sanjay.store.Users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class Starting_Letter implements ConstraintValidator<Starting,String> {

    public boolean isValid(String value, ConstraintValidatorContext c)
    {
        if(value==null)
            return true;
        return Character.isUpperCase(value.charAt(0));
    }

}
