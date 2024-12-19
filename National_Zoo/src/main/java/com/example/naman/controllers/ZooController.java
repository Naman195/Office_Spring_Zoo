package com.example.naman.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.CreateZooDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.services.ZooService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.Multipart;

@RestController
@RequestMapping("/zoo")
public class ZooController {

	@Autowired
	private ZooService zooService;
	
	@PreAuthorize("hasRole('admin')")
	@PostMapping(value = "/add", consumes = { "multipart/form-data" })
	public ResponseEntity<?> createZoo(@RequestPart("zoo") String zooJson, 
            @RequestPart(value = "file", required = false) MultipartFile file)  {

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        CreateZooDTO zoo = objectMapper.readValue(zooJson, CreateZooDTO.class);
	     
	        zooService.createZoo(zoo, file);
	        return ResponseEntity.ok("Zoo created successfully!");	
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
	    } catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	    }
	}
	
	@GetMapping("/fetchall")
	public ResponseEntity<Page<ZooResponseDTO>> getAllZoo(@RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "3") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<ZooResponseDTO> zoos = zooService.getAllZoo(pageable);
		return ResponseEntity.ok(zoos);
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ZooResponseDTO> getZooById(@PathVariable Long id) {
		return ResponseEntity.ok(zooService.getZooById(id));
	}	
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/delete/{id}")
	public String deleteZoo(@PathVariable Long id) {
		zooService.deleteZooById(id);
		return "Zoo Deleted SuccessFully";
	}
	
	@PreAuthorize("hasRole('admin')")
	@PatchMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateZoo(@RequestPart("zoo") String zooJson, 
            @RequestPart(value = "file", required = false) MultipartFile file, 
            @PathVariable Long id) throws IOException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	        CreateZooDTO zoo = objectMapper.readValue(zooJson, CreateZooDTO.class);
			return ResponseEntity.ok(zooService.updateZooById(zoo, file, id));
		} catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
	    }
		catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	    }
		
		
	}
	
    
    @GetMapping("/search")
    public ResponseEntity<List<ZooResponseDTO>> searchByNameOrLocation(
            @RequestParam String searchItem) {
    	List<ZooResponseDTO> searchList = zooService.searchByNameOrLocation(searchItem);
        return ResponseEntity.ok(searchList);
    }
    
}
