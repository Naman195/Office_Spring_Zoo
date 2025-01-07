package com.example.naman.entities;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class RefreshToken {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;
	
	@Column(name = "refresh_token", unique = true)
	private String refreshToken;
	
	@OneToOne
	
	private User user;
	
	private LocalDateTime expireAt;
	
}
