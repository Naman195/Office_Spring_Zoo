package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	
	
}
