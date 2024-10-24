package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.State;

public interface StateRepository extends JpaRepository<State, Long> 
{
	List<State> findByCountryCountryId(Long countryId);
}
