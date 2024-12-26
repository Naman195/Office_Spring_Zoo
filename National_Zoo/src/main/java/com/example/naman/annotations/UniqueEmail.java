package com.example.naman.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


//Declaring an annotation for validating URLs
//Retention is the time period
//Till what time you want your annotation to work around.
@Retention(RetentionPolicy.RUNTIME)

//Target gives you that on what instance you can use this annotation
@Target(ElementType.FIELD)

//Defined the constraint and the Validator Class!
@Constraint(validatedBy  = UniqueEmailImpl.class)

public @interface UniqueEmail {
	
	// Error message which would be sent, You can set default value
    String message() default "Email is Already Exist!";

    // Group of constraints
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
