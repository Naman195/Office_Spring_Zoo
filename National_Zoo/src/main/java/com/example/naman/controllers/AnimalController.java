package com.example.naman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Animal;
import com.example.naman.services.AnimalService;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
	
	@Autowired
	private AnimalService animalService;
	
	@PreAuthorize("hasRole('admin')")
	@PostMapping("/add")
	public Animal saveAnimal(@RequestBody Animal animal) {
		
		return animalService.addAnimal(animal);
	}
	
	
	@GetMapping("/all-ani")
	public ResponseEntity<Page<Animal>> getAllAnimals(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "4") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<Animal> animals = animalService.getAllAnimals(pageable);
		return ResponseEntity.ok(animals);
	}
	
	@GetMapping("/zoo-ani/{id}")
	public ResponseEntity<Page<Animal>> getAnimalsByZooId(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "3") int size, @PathVariable Long id){
		Pageable pageable = PageRequest.of(page, size);
		Page<Animal> animals = animalService.getAnimalByZooId(id, pageable);
		return ResponseEntity.ok(animals);
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
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/del/{id}")
	public String deleteAnimalById(@PathVariable Long id)
	{
		animalService.deletedAnimal(id);
		return "Animal Deleted SuccessFully";
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/update/{id}")
	public Animal updateAnimalDetail(@RequestBody Animal animal, @PathVariable Long id)
	{
		return animalService.updateAnimalById(animal, id);
	}
	
}
