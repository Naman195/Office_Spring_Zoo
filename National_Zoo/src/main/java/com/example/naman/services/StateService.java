package com.example.naman.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.State;
import com.example.naman.repositories.StateRepository;

@Service
public class StateService {
	
	@Autowired
	private StateRepository stateRepository;

	public List<State> getAllStatesByCountryId(Long id)
	{
		return stateRepository.findByCountryCountryId(id);
	}

}
