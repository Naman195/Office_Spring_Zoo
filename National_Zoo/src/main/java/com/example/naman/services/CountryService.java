package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Country;
import com.example.naman.repositories.CountryRepository;

/**
 * Country Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;
	
	/**
	 * this method is used for fetch All the Countries.
	 * @return Get List of All Countries.
	 * 
	 * @author Naman Arora
	 * */
	
	public List<Country> getAllCountry()
	{
		return countryRepository.findAll();
		
	}
	
	
	
}
