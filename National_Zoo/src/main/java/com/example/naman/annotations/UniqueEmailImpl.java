package com.example.naman.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.naman.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailImpl implements ConstraintValidator<UniqueEmail, String> {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		try {
            return !userRepository.existsByEmail(email);
        } catch (Exception ex) {
            // Log the error and fail gracefully
            System.err.println("Error during email validation: " + ex.getMessage());
            return false;
        }
		
	}
	

}
