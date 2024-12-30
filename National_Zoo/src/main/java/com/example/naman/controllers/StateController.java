package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.State;
import com.example.naman.services.StateService;

/**
 * State Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/state")
public class StateController  {

	@Autowired
	private StateService stateService;
	
	/**
	 * this Controller is used for fetch All States related to Particular CountryId
	 * @param  countryId
	 * @return List of States
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/{countryId}")
	public List<State> getAllStatesByCountryId(@PathVariable Long countryId){
		return stateService.getAllStatesByCountryId(countryId);		
	}
}
