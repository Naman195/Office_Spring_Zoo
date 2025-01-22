package com.example.naman.configs;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.ResponseUserDTO;
import com.example.naman.DTOS.UserResponse;
import com.example.naman.entities.RefreshToken;
import com.example.naman.entities.Token;
import com.example.naman.entities.User;
import com.example.naman.repositories.TokenRepository;
import com.example.naman.repositories.UserRepository;
import com.example.naman.services.JwtService;
import com.example.naman.services.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		try {
			
			OAuth2User  principal = (OAuth2User ) authentication.getPrincipal();
			String email = principal.getAttribute("email"); 
			
			User user = userRepository.findByEmail(email).orElse(null);
			if (user == null) {
	           
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
			
			ResponseUserDTO userRes = modelMapper.map(user, ResponseUserDTO.class);
			
			 String jwtToken = jwtService.generateToken(user);
		        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
		        
		      
		        Token token = Token.builder()
		                .tokenValue(jwtToken)
		                .userId(user.getUserId())
		                .expiresAt(jwtService.getExp(jwtToken))
		                .build();
		        tokenRepository.save(token);
		        
		        UserResponse userResponse = UserResponse.builder().userId(user.getUserId()).username(user.getUsername()).token(jwtToken).message("LoggedIn Successfully").user(userRes).refreshToken(refreshToken.getRefreshToken()).build();
		        
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        
		        ObjectMapper mapper = new ObjectMapper();
		        
		        response.getWriter().write(mapper.writeValueAsString(userResponse));
		        response.setStatus(HttpServletResponse.SC_OK);
		        
		        response.sendRedirect("http://localhost:3000/dashboard");

			
		} catch (Exception e) {
			System.out.printf("Exception occured", e.getMessage());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}

	
}
