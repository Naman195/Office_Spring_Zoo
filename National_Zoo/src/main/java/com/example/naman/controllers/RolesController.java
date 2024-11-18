package com.example.naman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.naman.DTOS.RoleResponseDTO;
import com.example.naman.services.RolesService;



@RestController
@RequestMapping("/api/role")
public class RolesController {
	
	@Autowired
	private RolesService rolesService;
	
	@GetMapping("/all")
	public ResponseEntity<List<RoleResponseDTO>> getRole()
	{
		return ResponseEntity.ok(rolesService.getRoles());
	}

}
