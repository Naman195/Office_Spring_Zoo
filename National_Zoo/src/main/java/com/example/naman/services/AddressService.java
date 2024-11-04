package com.example.naman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.repositories.AddressRepository;
import com.example.naman.repositories.CityRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	
	public Address createAddress(Address address)
	{
		return addressRepository.save(address);
		
	}
}
