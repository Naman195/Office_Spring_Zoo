package com.example.naman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Address;
import com.example.naman.services.AddressService;

@RestController
@RequestMapping("/api/auth")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@PostMapping("/save-add")
	public Address saveAddress(@RequestBody Address address) {
		return addressService.createAddress(address);
	}
}
