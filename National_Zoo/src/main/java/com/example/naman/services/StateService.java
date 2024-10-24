package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.State;
import com.example.naman.repositories.CountryRepository;
import com.example.naman.repositories.StateRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class StateService {

	
	private final  StateRepository stateRepository;
	
	private final CountryRepository countryRepository;
	
	
	
	public List<State> getStatesByCountryId(Long id)
	{
		return stateRepository.findByCountryCountryId(id);
	}

//	public List<State> getStateByCountryName(String name){
//		List<State> states = stateRepository.findByName(name);
//		return states;
//	}
	
	
}
