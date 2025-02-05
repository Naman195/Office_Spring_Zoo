package com.example.naman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Token;
import com.example.naman.repositories.TokenRepository;

@Service
public class TokenCache {
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Cacheable(value = "token", key = "#token")
	public Token findTokenByToken(String token) {
		return tokenRepository.findByTokenValue(token);
	}

}
