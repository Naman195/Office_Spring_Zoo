package com.example.naman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Address;
import com.example.naman.services.AddressService;


/**
 * Address Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	/**
	 * this controller is used for save the Address.
	 * @param Address
	 * @return Address
	 * 
	 * @author Naman Arora
	 * */
	
	@PostMapping("/save")
	public Address saveAddress(@RequestBody Address address) 
	{
		return addressService.createAddress(address);
	}
	
}
