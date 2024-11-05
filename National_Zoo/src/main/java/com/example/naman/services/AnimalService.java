package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Animal;
import com.example.naman.repositories.AnimalRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	
	public Animal addAnimal(Animal animal)
	{
		return animalRepository.save(animal);
	}
	
	public List<Animal> getAllAnimals()
	{
		List<Animal> animalsList =  animalRepository.findAll();
		List<Animal> filteredList = animalsList.stream().filter(animal -> !animal.isArchieved()).collect(Collectors.toList());
		return filteredList;

		
	}
	
	public Animal getAnimalById(Long id)
	{
		return animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal  Not Found"));
		
	}
	
	public void deletedAnimal(Long id)
	{
		Animal ani = animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal  Not Found"));
		ani.setArchieved(!ani.isArchieved()); 
		animalRepository.save(ani);
	}
	
	public Animal updateAnimalById(Animal animal, Long id)
	{
		Animal ani = animalRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal  Not Found"));
		ani.setAnimalName(animal.getAnimalName());
		ani.setAnimalType(animal.getAnimalType());
		ani.setZoo(animal.getZoo());
		ani.setUpdatedAt(animal.getUpdatedAt());
		ani.setUpdatedBy(animal.getUpdatedBy());
		
		return animalRepository.save(ani);
		

	}
	
	
}
