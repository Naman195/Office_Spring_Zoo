package com.example.naman.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.entities.RefreshToken;
import com.example.naman.entities.User;
import com.example.naman.repositories.RefreshTokenRepository;
import com.example.naman.repositories.UserRepository;

@Service
public class RefreshTokenService {

	public long rereshTokenValidity = 6*60;
	
	@Autowired
	private RefreshTokenRepository tokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public RefreshToken createRefreshToken(String userName) {
		User user = userRepository.findByuserName(userName).get();
		RefreshToken refreshToken = user.getRefreshToken();
		if(refreshToken == null) {
			refreshToken = RefreshToken.builder()
					.refreshToken(UUID.randomUUID().toString())
					.expireAt(LocalDateTime.now().plusSeconds(rereshTokenValidity)).user(user).build();
		}
		else {
			refreshToken.setExpireAt(LocalDateTime.now().plusSeconds(rereshTokenValidity));
		}
		
		user.setRefreshToken(refreshToken);
		tokenRepository.save(refreshToken);
		
		return refreshToken;
		
		
	}
	
	public RefreshToken verifyRefreshToken(String refreshToken) {
		RefreshToken refreshTokenObj = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("refresh Token is invalid"));
		
		if(refreshTokenObj.getExpireAt().compareTo(LocalDateTime.now())  < 0) {
			tokenRepository.delete(refreshTokenObj);
//			throw new  RuntimeException("Refresh Token Expired!!");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
		}
		
		return refreshTokenObj;
	}
	
}
