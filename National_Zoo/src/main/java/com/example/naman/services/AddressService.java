package com.example.naman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Address;
import com.example.naman.repositories.AddressRepository;

/**
 * Address Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */
@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	/**
	 * this method is used for save the Address.
	 * @param Address
	 * @return Address
	 * 
	 * @author Naman Arora
	 * */
	
	public Address createAddress(Address address)
	{
		return addressRepository.save(address);
		
	}
}
