package com.example.naman.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.naman.repositories.UserRepository;
import com.example.naman.services.UserDetailServiceImpl;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 29-Nov-2024
 */

@Configuration
public class ApplicationConfiguration {
	
    private final UserRepository userRepository;
    
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        return username -> userRepository.findByuserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}