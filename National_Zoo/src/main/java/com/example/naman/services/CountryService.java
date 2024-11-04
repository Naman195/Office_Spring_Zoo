package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Country;
import com.example.naman.repositories.CountryRepository;

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;
	
	
	public List<Country> getAllCountry()
	{
		return countryRepository.findAll();
		
	}
	
	
	
}
