package com.example.naman.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.AddressDTO;
import com.example.naman.DTOS.CreateZooDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.entities.Zoo;
import com.example.naman.exceptions.ResourceNotFoundException;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.ZooRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ZooService {
	
	@Autowired
	private  ZooRepository zooRepository; 
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	

	public void createZoo(CreateZooDTO zoo)
	{
		try {
	        Zoo newZoo = modelMapper.map(zoo, Zoo.class);
	        zooRepository.save(newZoo);
	    } catch (Exception e) {
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create zoo: " + e.getMessage());

	    }
	}
	
	
	public Page<ZooResponseDTO> getAllZoo(Pageable pageable)
	{
	    // Fetch all Zoo entities
	    Page<Zoo> allZoo = zooRepository.findByArchievedFalse(pageable);

	    // Map the list of Zoo entities to a list of ZooResponseDTOs
	    List<ZooResponseDTO> zooResponseDTOs = allZoo.getContent().stream()
	            .map(zoo -> modelMapper.map(zoo, ZooResponseDTO.class))
	            .collect(Collectors.toList());

	    // Return a new Page with the DTOs
	    return new PageImpl<>(zooResponseDTOs, pageable, allZoo.getTotalElements());
	}

	
	public ZooResponseDTO getZooById(Long id)
	{
		Zoo zoo =  zooRepository.findById(id)
				.filter(z -> !z.isArchieved())
				.orElseThrow(() ->  new ResourceNotFoundException("Zoo not found with ID: " + id));
		return modelMapper.map(zoo, ZooResponseDTO.class);
		
	}
	
	public void deleteZooById(Long id) {
		Zoo zoo = zooRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zoo Not Found By Id"));
		zoo.setArchieved(!zoo.isArchieved());
		zooRepository.save(zoo);
	}
	
	
	public ZooResponseDTO updateZooById(CreateZooDTO updateZooDTO, Long id)
	{
		Zoo existingZoo = zooRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zoo Not Found By Id"));
		modelMapper.map(updateZooDTO, existingZoo);
		Long cityId = updateZooDTO.getAddress().getCity().getCityId();
		City city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));
		existingZoo.getAddress().setCity(city);
        Zoo updatedZoo = zooRepository.save(existingZoo);
		
		return modelMapper.map(updatedZoo, ZooResponseDTO.class);
	}
	
	public List<Zoo> searchZoosByName(String name) {
        return zooRepository.findByZooNameContainingIgnoreCase(name);
    }
	
//	public List<Zoo> searchZooByCountry(String countryName){
//		return zooRepository.findByAddress_City_State_Country_CountryNameContainingIgnoreCase(countryName);
//	}
	
	public List<Zoo> searchZoosByLocation(String country, String state, String city){
    	if(city != null && !city.isEmpty()) {
    		return zooRepository.findByAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCaseAndAddress_City_CityNameContainingIgnoreCase(country, state, city);
    	}
    	
    	else if(state != null && !state.isEmpty()) {
    		return zooRepository.findByAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCase(country, state);
    	} 
    	else {
    		return zooRepository.findByAddress_City_State_Country_CountryNameContainingIgnoreCase(country);
    	}
    	
    	
    }
	
	
	
}
