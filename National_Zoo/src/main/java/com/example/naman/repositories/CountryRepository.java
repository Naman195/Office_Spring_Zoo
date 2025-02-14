package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Country;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

public interface CountryRepository extends JpaRepository<Country, Long> {
	
}
