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

import com.example.naman.entities.Zoo;
import com.example.naman.services.ZooService;

@RestController
@RequestMapping("/api/zoo")
public class ZooController {

	@Autowired
	private ZooService zooService;
	
	@PreAuthorize("hasRole('admin')")
	@PostMapping("/create-zoo")
	public Zoo createZoo(@RequestBody Zoo zoo) {
		return zooService.createZoo(zoo); 
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<Zoo>> getAllZoo(@RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "3") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<Zoo> zoos = zooService.getAllZoo(pageable);
		return ResponseEntity.ok(zoos);
		
	}
	
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getZooById(@PathVariable Long id) {
		Zoo zoo = zooService.getZooById(id);
		
		if(zoo.isArchieved()) {
			return ResponseEntity.status(404).body("Zoo Not Found");
		}
		
		return ResponseEntity.ok().body(zoo);
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/del/{id}")
	public String deleteZoo(@PathVariable Long id) {
		zooService.deleteZooById(id);
		return "Zoo Deleted SuccessFully";
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/update/{id}")
	public Zoo updateZoo(@RequestBody Zoo zoo, @PathVariable Long id) {
		return zooService.updateZooById(zoo, id);
	}
	
}
