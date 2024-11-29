package com.example.naman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") 
public class NationalZooApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(NationalZooApplication.class, args);
	}
}