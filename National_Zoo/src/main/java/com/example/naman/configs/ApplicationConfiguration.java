package com.example.naman.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 29-Nov-2024
 */

@Configuration
public class ApplicationConfiguration {
	
 
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}