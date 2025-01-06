package com.example.naman.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.RefreshToken;
import com.example.naman.entities.User;
import com.example.naman.repositories.RefreshTokenRepository;
import com.example.naman.repositories.UserRepository;

@Service
public class RefreshTokenService {

	public long rereshTokenValidity = 5*60*60*1000;
	
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
					.expireAt(Instant.now().plusMillis(rereshTokenValidity)).user(user).build();
		}
		else {
			refreshToken.setExpireAt(Instant.now().plusMillis(rereshTokenValidity));
		}
		
		user.setRefreshToken(refreshToken);
		tokenRepository.save(refreshToken);
		
		return refreshToken;
		
		
	}
	
	public RefreshToken verifyRefreshToken(String refreshToken) {
		RefreshToken refreshTokenObj = tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("refresh Token is invalid"));
		
		if(refreshTokenObj.getExpireAt().compareTo(Instant.now())  < 0) {
			tokenRepository.delete(refreshTokenObj);
			throw new  RuntimeException("Refresh Token Expired!!");
		}
		
		return refreshTokenObj;
	}
	
}
