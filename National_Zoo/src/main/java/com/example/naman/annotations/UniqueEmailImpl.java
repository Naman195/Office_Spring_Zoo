package com.example.naman.annotations;


import org.springframework.beans.factory.annotation.Autowired;

import com.example.naman.entities.User;
import com.example.naman.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailImpl implements ConstraintValidator<UniqueEmail, String> {
	@Autowired
	private UserRepository repository;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		try {
			

			System.out.println(repository.existsByEmail(email));
			return !repository.existsByEmail(email);
		} catch (Exception ex) {

			System.err.println("Error in email validation: " + ex.getMessage());
			return false;
		}

	}

}
