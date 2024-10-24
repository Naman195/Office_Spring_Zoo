package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Country;
import com.example.naman.services.CountryService;

@RestController
@RequestMapping("/api/zoo")
public class CountryControllers {

	@Autowired
	private CountryService countryService;
	
	
	@GetMapping("/countries")
	public List<Country> getAllCountries(){
		List<Country> countries =  countryService.getAllCountry();
		return countries;
	}
}
