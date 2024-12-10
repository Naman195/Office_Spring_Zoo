package com.example.naman.controllers;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.CreateZooDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Zoo;
import com.example.naman.services.ZooService;

@RestController
@RequestMapping("/api/zoo")
public class ZooController {

	@Autowired
	private ZooService zooService;
	
	@PreAuthorize("hasRole('admin')")
	@PostMapping("/create")
	public ResponseEntity<?> createZoo(@RequestBody CreateZooDTO zoo) {
		try {
	        zooService.createZoo(zoo);
	        return ResponseEntity.ok("Zoo created successfully!");
	    } catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	    }
	}
	
	@GetMapping("/allZoo")
	public ResponseEntity<Page<ZooResponseDTO>> getAllZoo(@RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "3") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<ZooResponseDTO> zoos = zooService.getAllZoo(pageable);
		return ResponseEntity.ok(zoos);
		
	}
	
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ZooResponseDTO> getZooById(@PathVariable Long id) {
		return ResponseEntity.ok(zooService.getZooById(id));
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/deleleZoo/{id}")
	public String deleteZoo(@PathVariable Long id) {
		zooService.deleteZooById(id);
		return "Zoo Deleted SuccessFully";
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/updateZoo/{id}")
	public ResponseEntity<ZooResponseDTO> updateZoo(@RequestBody CreateZooDTO zoo, @PathVariable Long id) {
		return ResponseEntity.ok(zooService.updateZooById(zoo, id));
	}
	
    
    @GetMapping("/search")
    public ResponseEntity<List<ZooResponseDTO>> searchByNameOrLocation(
            @RequestParam String searchItem) {
    	List<ZooResponseDTO> searchList = zooService.searchByNameOrLocation(searchItem);
        return ResponseEntity.ok(searchList);
    }
    
}
