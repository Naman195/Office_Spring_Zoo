package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Country;
import com.example.naman.services.CountryService;

/**
 * Country Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/country")
public class CountryControllers {

	@Autowired
	private CountryService countryService;
	
	/**
	 * this controller is used for fetch All the Countries.
	 * @return Get List of All Countries.
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/all")
	public List<Country> getAllCountries(){
		return countryService.getAllCountry();		
	}
}
