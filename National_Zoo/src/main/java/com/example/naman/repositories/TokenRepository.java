package com.example.naman.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	
	public Token findByTokenValue(String token);
	
	public void deleteByExpiresAtBefore(LocalDateTime now);

}
