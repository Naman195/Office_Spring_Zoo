package com.example.naman.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	public Optional<RefreshToken>  findByRefreshToken(String token);
	
//	public List<RefreshToken> findByExpireAtBefore(LocalDateTime now);

}
