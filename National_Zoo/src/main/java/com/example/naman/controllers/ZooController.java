package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.entities.Zoo;
import com.example.naman.services.ZooService;

@RestController
@RequestMapping("/api/zoo")
public class ZooController {

	@Autowired
	private ZooService zooService;
	
	@PostMapping("/create-zoo")
	public Zoo createZoo(@RequestBody Zoo zoo) {
		return zooService.createZoo(zoo); 
	}
	
	@GetMapping("/all")
	public List<Zoo> getAllZoo()
	{
		return zooService.getAllZoo();
	}
	
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getZooById(@PathVariable Long id) {
		Zoo zoo = zooService.getZooById(id);
		
		if(zoo.isArchieved()) {
			return ResponseEntity.status(404).body("Zoo Not Found");
		}
		
		return ResponseEntity.ok().body(zoo);
	}
	
	
	@PatchMapping("/del/{id}")
	public String deleteZoo(@PathVariable Long id) {
		zooService.deleteZooById(id);
		return "Zoo Deleted SuccessFully";
	}
	
	@PatchMapping("/update/{id}")
	public Zoo updateZoo(@RequestBody Zoo zoo, @PathVariable Long id) {
		return zooService.updateZooById(zoo, id);
	}
	
}
