package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.State;
import com.example.naman.repositories.StateRepository;


/**
 * State Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@Service
public class StateService {
	
	@Autowired
	private StateRepository stateRepository;

	/**
	 * this method is used for fetch All the States via CountryId.
	 * @param countryId
	 * @return Get List of All states.
	 * 
	 * @author Naman Arora
	 * */
	
	public List<State> getAllStatesByCountryId(Long id)
	{
		return stateRepository.findByCountryCountryId(id);
	}

}
