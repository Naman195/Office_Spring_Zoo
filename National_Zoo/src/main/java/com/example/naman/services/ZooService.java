package com.example.naman.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Zoo;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.ZooRepository;

@Service
public class ZooService {
	
	@Autowired
	private  ZooRepository zooRepository; 
	
	

	public Zoo createZoo(Zoo zoo)
	{
		return zooRepository.save(zoo);
	}
	
	public Page<Zoo> getAllZoo(Pageable pageable) {
		
		return zooRepository.findByArchievedFalse(pageable);
//		List<Zoo> allZoo =  zooRepository.findAll();
//		List<Zoo> filteredZoo = allZoo.stream().filter(zoo -> !zoo.isArchieved()).collect(Collectors.toList());
//		return filteredZoo;
	}
	
	public Zoo getZooById(Long id) {
		return zooRepository.findById(id).orElseThrow(() -> new RuntimeException("Zoo not Found"));
	}
	
	public void deleteZooById(Long id) {
		Zoo zoo = zooRepository.findById(id).orElseThrow(() -> new RuntimeException("Zoo Not Found By Id"));
		zoo.setArchieved(!zoo.isArchieved());
		zooRepository.save(zoo);
	}
	
	
	public Zoo updateZooById(Zoo zoo, Long id)
	{
		Zoo existingZoo = zooRepository.findById(id).orElseThrow(() -> new RuntimeException("Zoo Not Found By Id"));
		existingZoo.setZooName(zoo.getZooName());
		existingZoo.setAddress(zoo.getAddress());
		existingZoo.setUpdatedBy(zoo.getUpdatedBy());
		existingZoo.setUpdatedAt(zoo.getUpdatedAt());
		return zooRepository.save(existingZoo);
	}
	
	
	
	
}
