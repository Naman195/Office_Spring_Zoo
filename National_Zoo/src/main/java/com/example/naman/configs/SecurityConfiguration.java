package com.example.naman.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
//    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private CustomOAuth2LoginSuccessHandler auth2LoginSuccessHandler;

    public SecurityConfiguration(
        JwtAuthenticationFilter jwtAuthenticationFilter
        
    ) {
        
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//        .httpBasic(httpBasic -> httpBasic.disable())
//        .formLogin((form) -> form.disable())
        .logout((logout) -> logout.disable())
        .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login", "/auth/create", "/login/oauth2/**", "/country/all", "/state/*", "/city/*", "/auth/forgotpassword", "/auth/verifyotp", "/auth/setpassword", "/auth/refresh", "/role/all", "/auth/hello", "/auth/user-info")
                        
                        .permitAll()
                        
                        .anyRequest()
                        .authenticated())
        				
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oauth2 -> oauth2.successHandler(auth2LoginSuccessHandler))
        		
        .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS.IF_REQUIRED));

return http.build();
    }

 
    	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH") );
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}