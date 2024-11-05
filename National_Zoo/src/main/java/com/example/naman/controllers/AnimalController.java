package com.example.naman.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Animal;
import com.example.naman.services.AnimalService;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
	
	@Autowired
	private AnimalService animalService;
	
	@PostMapping("/add")
	public Animal saveAnimal(Animal animal) {
		
		return animalService.addAnimal(animal);
	}
	
	
	@GetMapping("/all-ani")
	public List<Animal> getAllAnimals()
	{
		return animalService.getAllAnimals();
	}
	
	
	@GetMapping("/ani-id/{id}")
	public ResponseEntity<?> getAnimalById(@PathVariable Long id)
	{
		Animal animal = animalService.getAnimalById(id);
		
		if(animal.isArchieved())
		{
			return ResponseEntity.status(404).body("Animal Not Found");
		}
		
		return ResponseEntity.ok().body(animal);
	}
	
	@PatchMapping("/del/{id}")
	public String deleteAnimalById(@PathVariable Long id)
	{
		animalService.deletedAnimal(id);
		return "Animal Deleted SuccessFully";
	}
	
	
	@PatchMapping("/update/{id}")
	public Animal UpdateAnimalDetail(@RequestBody Animal animal, @PathVariable Long id)
	{
		return animalService.updateAnimalById(animal, id);
	}
	
	
	
	
	
	
	
	

}
