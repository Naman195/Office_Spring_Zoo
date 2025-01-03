package com.example.naman.services;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.naman.repositories.TokenRepository;

@Service
public class TokenService 
{
	@Autowired
	private TokenRepository tokenRepository;
	
	/**
	 * 
	 */
	@Scheduled(fixedRate = 6, timeUnit = TimeUnit.MINUTES)
    void deleteExpiredTokens()
	{
		System.out.println("Scheduler is Running in every 1 Second");
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.delete(tokenRepository.findByExpiresAtBefore(now));
    }
}
