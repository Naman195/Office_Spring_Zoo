package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.DTOS.RoleResponseDTO;
import com.example.naman.entities.Roles;
import com.example.naman.repositories.RoleRepository;

@Service
public class RolesService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public List<RoleResponseDTO> getRoles()
	{
		List<Roles> roles =  roleRepository.findAll();
		
		return roles.stream().map(role -> modelMapper.map(role, RoleResponseDTO.class)).collect(Collectors.toList());
		
		
	}
	
}
