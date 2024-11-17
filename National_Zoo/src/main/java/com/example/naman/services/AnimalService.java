package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.naman.DTOS.AnimalResponseDTO;
import com.example.naman.DTOS.CreateAnimalDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Animal;
import com.example.naman.entities.City;
import com.example.naman.entities.Zoo;
import com.example.naman.exceptions.ResourceNotFoundException;
import com.example.naman.repositories.AnimalRepository;
import com.example.naman.repositories.ZooRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ZooRepository zooRepository;
	
	public AnimalResponseDTO addAnimal(CreateAnimalDTO animal)
	{
		Animal addAnimal = modelMapper.map(animal, Animal.class);
		Animal savedAnimal =  animalRepository.save(addAnimal);
		
		AnimalResponseDTO responseDTO = modelMapper.map(savedAnimal, AnimalResponseDTO.class);
		return responseDTO;
	}
	
	public Page<Animal> getAllAnimals(Pageable pageable)
	{
		return animalRepository.findByArchievedFalse(pageable);
		
		
	}
	
	public Page<AnimalResponseDTO> getAnimalByZooId(Long id, Pageable pageable)
	{
		
		Page<Animal> allanimalsInZoo =  animalRepository.findByArchievedFalseAndZooZooId(id, pageable);
		
		
		// Map the list of Animals entities to a list of AnimalResponseDTOs
	    List<AnimalResponseDTO> animalResponseDTOs = allanimalsInZoo.getContent().stream()
	            .map(animal -> modelMapper.map(animal, AnimalResponseDTO.class))
	            .collect(Collectors.toList());

	    // Return a new Page with the DTOs
	    return new PageImpl<>(animalResponseDTOs, pageable, allanimalsInZoo.getTotalElements());
		
	}
	
	public AnimalResponseDTO getAnimalById(Long id)
	{
		Animal animal =  animalRepository.findById(id).filter(ani -> !ani.isArchieved())
				.orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		return modelMapper.map(animal, AnimalResponseDTO.class);
	}
	
	public void deletedAnimal(Long id)
	{
		Animal ani = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		ani.setArchieved(!ani.isArchieved()); 
		animalRepository.save(ani);
	}
	
	public AnimalResponseDTO updateAnimalById(CreateAnimalDTO updateAnimalDTO, Long id)
	{
		Animal existingAnimal = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		modelMapper.map(updateAnimalDTO, existingAnimal);
		Long zooId = updateAnimalDTO.getZoo().getZooId();
		Zoo zoo = zooRepository.findById(zooId).orElseThrow(() -> new ResourceNotFoundException("Zoo not found with ID: " + zooId));
		existingAnimal.setZoo(zoo);
		Animal updatedAnimal = animalRepository.save(existingAnimal);
		return modelMapper.map(updatedAnimal, AnimalResponseDTO.class);
	
	}
	
	
}
