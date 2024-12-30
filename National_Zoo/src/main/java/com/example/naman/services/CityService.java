package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.City;
import com.example.naman.repositories.CityRepository;

/**
 * City Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;
	
	/**
	 * this method is used for fetch All the Cities via StateId.
	 * @param stateId
	 * @return Get List of All Cities.
	 * 
	 * @author Naman Arora
	 * */
	
	public List<City> getAllCitiesByStateId(Long stateId){
		return cityRepository.findByStateStateId(stateId);
	}
	
}
