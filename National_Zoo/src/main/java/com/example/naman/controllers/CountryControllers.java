package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Country;
import com.example.naman.services.CountryService;

@RestController
@RequestMapping("/country")
public class CountryControllers {

	@Autowired
	private CountryService countryService;
	
	
	@GetMapping("/all")
	public List<Country> getAllCountries(){
		return countryService.getAllCountry();		
	}
}
