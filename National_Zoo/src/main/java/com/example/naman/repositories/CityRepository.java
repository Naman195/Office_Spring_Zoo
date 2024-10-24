package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.City;

public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findByStateStateId(Long stateId);
}
