package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.DTOS.RoleResponseDTO;
import com.example.naman.services.RolesService;


/**
 * Roles Controller
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

@RestController
@RequestMapping("/role")
public class RolesController {
	
	@Autowired
	private RolesService rolesService;
	
	/**
	 * this controller is used for fetch roles;
	 * 
	 * @return list of Role.
	 * 
	 * @author Naman Arora
	 * */
	
	@GetMapping("/all")
	public ResponseEntity<List<RoleResponseDTO>> getRole()
	{
		return ResponseEntity.ok(rolesService.getRoles());
	}

}
