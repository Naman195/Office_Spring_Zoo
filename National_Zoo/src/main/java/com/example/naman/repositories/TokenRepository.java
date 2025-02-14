package com.example.naman.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Token;

/**
 * 
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
	
	public Token findByTokenValue(String token);
	
	public List<Token> findByExpiresAtBefore(LocalDateTime now);
	
	

}
