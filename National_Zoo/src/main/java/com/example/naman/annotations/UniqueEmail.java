package com.example.naman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
  

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailImpl.class )
public @interface UniqueEmail 
{
	String message() default "Email is already exists";
	Class<?>[] groups() default {}; 
	Class<? extends Payload>[] payload() default {};

}
