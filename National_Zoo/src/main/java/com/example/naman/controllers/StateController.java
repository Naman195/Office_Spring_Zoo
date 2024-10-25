package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.State;
import com.example.naman.services.StateService;

@RestController
@RequestMapping("/api/auth")
public class StateController  {

	@Autowired
	private StateService stateService;
	
	
	@GetMapping("/state/{id}")
	public List<State> getAllStates(@PathVariable Long id){
		List<State> states = stateService.getStatesByCountryId(id);
		return states;
	}
}
