package com.example.naman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NationalZooApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(NationalZooApplication.class, args);
	}
	
}