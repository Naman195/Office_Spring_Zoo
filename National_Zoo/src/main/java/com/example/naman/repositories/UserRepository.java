package com.example.naman.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.User;

/**
 * 
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */
public interface UserRepository extends JpaRepository<User, Long> {
//	public User findByuserName(String username);
	
	public Optional<User> findByuserName(String username);
	
	public Optional<User> findByEmail(String email);
 
	public boolean existsByEmail(String email);

}
