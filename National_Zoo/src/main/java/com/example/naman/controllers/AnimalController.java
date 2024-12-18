package com.example.naman.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
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

import com.example.naman.DTOS.AnimalResponseDTO;
import com.example.naman.DTOS.CreateAnimalDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Animal;
import com.example.naman.entities.Zoo;
import com.example.naman.services.AnimalService;
import com.example.naman.services.ZooService;

@RestController
@RequestMapping("/animal")
public class AnimalController {
	
	@Autowired
	private AnimalService animalService;
	
	@Autowired
	private ZooService zooService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PreAuthorize("hasRole('admin')")	
	@PostMapping("/add")
	public ResponseEntity<AnimalResponseDTO> saveAnimal(@RequestBody CreateAnimalDTO animalDTO) {
		
		ZooResponseDTO zooResponseDTO = zooService.getZooById(animalDTO.getZoo().getZooId());
		Zoo zoo = modelMapper.map(zooResponseDTO, Zoo.class);
		Animal animal = modelMapper.map(animalDTO, Animal.class);
		animal.setZoo(zoo);
		AnimalResponseDTO animalResponseDTO  = animalService.addAnimal(animalDTO);
		return ResponseEntity.ok(animalResponseDTO);		
	}
	
	@GetMapping("/all/{id}")
	public ResponseEntity<Page<AnimalResponseDTO>> getAnimalsByZooId(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "3") int size, @PathVariable Long id){
		Pageable pageable = PageRequest.of(page, size);
		Page<AnimalResponseDTO> animals = animalService.getAnimalByZooId(id, pageable);
		return ResponseEntity.ok(animals);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AnimalResponseDTO> getAnimalById(@PathVariable Long id)
	{
		return ResponseEntity.ok(animalService.getAnimalById(id));
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/delete/{id}")
	public String deleteAnimalById(@PathVariable Long id)
	{
		animalService.deletedAnimal(id);
		return "Animal Deleted SuccessFully";
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/update/{id}")
	public ResponseEntity<AnimalResponseDTO> updateAnimalDetail(@RequestBody CreateAnimalDTO animal, @PathVariable Long id)
	
	{
		return ResponseEntity.ok(animalService.updateAnimalById(animal, id));
	}
	
	 @GetMapping("/search")
	    public List<Animal> searchByNameOrType(@RequestParam String searchTerm, @RequestParam Long zooId) {
	        return animalService.searchByNameOrType(searchTerm, zooId);
	    }
	 
	 @PreAuthorize("hasRole('admin')")
	 @GetMapping("getzoolist/{zooid}")
	 public ResponseEntity<List<ZooResponseDTO>> findAllZooExceptCurrent(@PathVariable Long zooid){
		 return ResponseEntity.ok(animalService.getAllZooExceptCurrentZoo(zooid));
	 }
	 
	 @PreAuthorize("hasRole('admin')")
	 @PatchMapping("/transfer/{animalid}/to/{newzooid}")
	 public ResponseEntity<AnimalResponseDTO> TransferAnimal(@PathVariable Long animalid, @PathVariable Long newzooid){
		 return ResponseEntity.ok(animalService.transferAnimal(animalid, newzooid));
	 }
	 
	 @PreAuthorize("hasRole('admin')")
	 @GetMapping("/history/{animalId}")
	 public ResponseEntity<?> getAnimalTransferHistory(@PathVariable Long animalId) {
	        return animalService.animalTransferHistory(animalId);
	    }
}
