package com.example.naman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.naman.entities.AuditorAwareImpl;
import com.example.naman.services.JavaSmtpGmailSenderService;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") 
public class NationalZooApplication {
	
	@Autowired
	 private JavaSmtpGmailSenderService senderService;
	public static void main(String[] args) {
		SpringApplication.run(NationalZooApplication.class, args);
	}
	
	 	@Bean
	    public AuditorAware<String> auditorProvider() {
	        return new AuditorAwareImpl();
	    }
	 	

}