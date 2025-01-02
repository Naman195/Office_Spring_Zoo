package com.example.naman.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.naman.repositories.TokenRepository;

@Service
public class TokenCleanupService {
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Scheduled(fixedRate = 3600000)
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByExpiresAtBefore(now);
    }

}
