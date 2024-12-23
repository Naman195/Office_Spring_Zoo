package com.example.naman.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Priviledges;
import com.example.naman.entities.Roles;
import com.example.naman.entities.RolesPriviledges;
import com.example.naman.repositories.RoleRepository;
import com.example.naman.repositories.RolesPriviledgesRepository;

@Service
public class RolesPriviledgesService {
	
	@Autowired
	private RolesPriviledgesRepository  rolesPriviledgesRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	public Set<Priviledges>  getPriviledgeForRole(String roleName) {
		Roles role = roleRepository.findByRoleName(roleName).orElseThrow(() ->  new RuntimeException("Role  Not Found"));
		
		List<RolesPriviledges> mappings = rolesPriviledgesRepository.findByRoles(role);
		
		Set<Priviledges> priviledges = new HashSet<>();
		
		for(RolesPriviledges mapping : mappings) {
			priviledges.add(mapping.getPriviledges());
		}
		return priviledges;
		
	}
	
	

}
