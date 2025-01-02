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
import com.example.naman.enums.MessageResponse;
import com.example.naman.services.ZooService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.Multipart;

/**
 * Zoo Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
  */

@RestController
@RequestMapping("/zoo")
public class ZooController {

	@Autowired
	private ZooService zooService;
	
	/**
	 * this controller is used for Add New Zoo
	 * @param zooJson, zooImage.
	 * @return "Zoo created successfully!"
	 * 
	 * @author Naman Arora
	 * */
	 
	@PreAuthorize("hasAuthority('create')")
	@PostMapping(value = "/add", consumes = { "multipart/form-data" })
	public ResponseEntity<?> createZoo(@RequestPart("zoo") String zooJson, 
            @RequestPart(value = "file", required = false) MultipartFile file)  
	{

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        CreateZooDTO zoo = objectMapper.readValue(zooJson, CreateZooDTO.class);
	     
	        zooService.createZoo(zoo, file);
	        return ResponseEntity.ok(MessageResponse.CREATE_ZOO.getMessage());	
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body(MessageResponse.JSON_INVALID.getMessage() + e.getMessage());
	    } catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	    }
	}
	
	/**
	 * this controller is used for get all Zoo List
	 * @param page, size
	 * @return List of Zoo as a Pagination Response.
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/fetchall")
	public ResponseEntity<Page<ZooResponseDTO>> getAllZoo(@RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "3") int size)
	{
		Pageable pageable = PageRequest.of(page, size);
		Page<ZooResponseDTO> zoos = zooService.getAllZoo(pageable);
		return ResponseEntity.ok(zoos);
		
	}
	
	
	/**
	 * this controller is used for getZoo By Id.
	 * @param ZooId.
	 * @return ZooResponseDTO Zoo
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/{id}")
	public ResponseEntity<ZooResponseDTO> getZooById(@PathVariable Long id) 
	{
		return ResponseEntity.ok(zooService.getZooById(id));
	}	
	
	/**
	 * this controller is used for archieved the Zoo.
	 * @param ZooId
	 * @return "Zoo Deleted SuccessFully"
	 * 
	 * @author Naman Arora
	 * */
	
	@PreAuthorize("hasAuthority('delete')")
	@PatchMapping("/delete/{id}")
	public String deleteZoo(@PathVariable Long id) 
	{
		zooService.deleteZooById(id);
		return MessageResponse.DELETE_ZOO.getMessage();
	}
	
	/**
	 * this controller is used for update the details of Zoo.
	 * @param zooid,  UpdatedzooJson, UpdatedzooImage.
	 * @return Updated ZooresponseDTO Zoo.
	 * 
	 * @author Naman Arora
	 * */
	
	@PreAuthorize("hasAuthority('update')")
	@PatchMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> updateZoo(@RequestPart("zoo") String zooJson, 
            @RequestPart(value = "file", required = false) MultipartFile file, 
            @PathVariable Long id) throws IOException
	{
		try {
			ObjectMapper objectMapper = new ObjectMapper();
	        CreateZooDTO zoo = objectMapper.readValue(zooJson, CreateZooDTO.class);
			return ResponseEntity.ok(zooService.updateZooById(zoo, file, id));
		} catch (JsonProcessingException e) {
	        return ResponseEntity.badRequest().body(MessageResponse.JSON_INVALID.getMessage() + e.getMessage());
	    }
		catch (ResponseStatusException e) {
	        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	    }
	}
	
	/**
	 * this controller is used for Search  a Zoo
	 * @param searchItem
	 * @return ZooList 
	 * 
	 * @author Naman Arora
	 * */
    
    @GetMapping("/search")
    public ResponseEntity<List<ZooResponseDTO>> searchByNameOrLocation(
            @RequestParam String searchItem)
    {
    	List<ZooResponseDTO> searchList = zooService.searchByNameOrLocation(searchItem);
        return ResponseEntity.ok(searchList);
    }
    
}
