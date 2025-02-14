package com.example.naman.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.naman.entities.AuditorAwareImpl;

/**
 * 
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class AuditConfiguration {

	/*
	 * Make Bean of AuditAwareImpl
	 */
	
	@Bean
    AuditorAware<String> auditorProvider() {
       return new AuditorAwareImpl();
   }
}
