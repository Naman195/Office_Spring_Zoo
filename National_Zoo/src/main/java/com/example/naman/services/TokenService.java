package com.example.naman.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Token;
import com.example.naman.repositories.TokenRepository;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

@Service
public class TokenService 
{
	@Autowired
	private TokenRepository tokenRepository;
	
//	@Autowired
//	private RefreshTokenRepository refreshTokenRepository;
	
	/**
	 * this method is used for delete the Expired JET tokens via Using Schedulers.
	 */
	@Scheduled(fixedRate = 6, timeUnit = TimeUnit.HOURS)
    void deleteExpiredTokens()
	{
		System.out.println("Scheduler is Running in every 1 Second");
        LocalDateTime now = LocalDateTime.now();
        List<Token> tokenObjectList = tokenRepository.findByExpiresAtBefore(now);
//        List<RefreshToken> refreshTokenList = refreshTokenRepository.findByExpireAtBefore(now);
        
        for(Token obj: tokenObjectList) {
        	tokenRepository.delete(obj);
        }
        
//        for(RefreshToken obj: refreshTokenList) {
//        	refreshTokenRepository.delete(obj);
//        }
//        
        
    }
}
