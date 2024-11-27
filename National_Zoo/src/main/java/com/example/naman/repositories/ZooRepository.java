package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Animal;
import com.example.naman.entities.Zoo;

public interface ZooRepository extends JpaRepository<Zoo, Long> {
	
	Page<Zoo> findByArchievedFalse(Pageable pageable);
	List<Zoo> findByZooNameContainingIgnoreCase(String zooName);
	List<Zoo> findByAddress_City_State_Country_CountryNameContainingIgnoreCase(String countryName);
	List<Zoo> findByAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCase(String countryName, String stateName);

    List<Zoo> findByAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCaseAndAddress_City_CityNameContainingIgnoreCase(
            String countryName, String stateName, String cityName);
    
    List<Zoo> findAllByZooIdNot(Long id);
}
