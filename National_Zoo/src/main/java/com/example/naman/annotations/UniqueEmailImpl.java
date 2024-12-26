package com.example.naman.annotations;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.naman.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailImpl implements ConstraintValidator<UniqueEmail, String> {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		boolean abc=userRepository.existsByEmail(email);
		System.out.println(abc);
		return !userRepository.existsByEmail(email);
		
		
	}
	

}
