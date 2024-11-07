package com.example.naman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.naman.entities.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") 
public class NationalZooApplication {

	public static void main(String[] args) {
		SpringApplication.run(NationalZooApplication.class, args);
	}
	
	 	@Bean
	    public AuditorAware<String> auditorProvider() {
	        return new AuditorAwareImpl();
	    }
	

}
