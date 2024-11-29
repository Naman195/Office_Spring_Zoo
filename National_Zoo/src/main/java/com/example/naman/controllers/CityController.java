package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.City;
import com.example.naman.services.CityService;

@RestController
@RequestMapping("/api/auth")
public class CityController {

	@Autowired
	private CityService cityService;
	
	@GetMapping("/cities/{cityId}")
	public List<City> getAllCities(@PathVariable  Long cityId)
	{
		return cityService.getAllCitiesById(cityId);
	}
}
