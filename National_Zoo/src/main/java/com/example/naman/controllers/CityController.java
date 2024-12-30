package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.City;
import com.example.naman.services.CityService;

/**
 * City Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityService cityService;
	
/**
 * this controller is used for fetch All Cities belongs to the stateid
 * @param stateId
 * @return List of cities.
 * 
 * @author Naman Arora
 * */
	
	@GetMapping("/{stateId}")
	public List<City> getAllCitiesByStateId(@PathVariable  Long stateId)
	{
		return cityService.getAllCitiesByStateId(stateId);
	}
}
