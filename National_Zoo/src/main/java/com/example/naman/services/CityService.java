package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.City;
import com.example.naman.repositories.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;
	
	public List<City> getAllCitiesByStateId(Long stateId){
		return cityRepository.findByStateStateId(stateId);
	}
	
}
