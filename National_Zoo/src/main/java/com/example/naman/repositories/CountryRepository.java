package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
	
}
