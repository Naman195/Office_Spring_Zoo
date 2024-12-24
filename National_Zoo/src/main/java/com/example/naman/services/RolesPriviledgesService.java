package com.example.naman.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.naman.entities.Roles;
import com.example.naman.repositories.RolesPriviledgesRepository;

@Service
public class RolesPriviledgesService
{
	@Autowired
	private RolesPriviledgesRepository  rolesPriviledgesRepository;
	
	public List<GrantedAuthority>  getPriviledgeForRole(Roles role) 
	{
		return rolesPriviledgesRepository.findByRoles(role)
				.stream()
				.map(rp -> new SimpleGrantedAuthority(rp.getPriviledges().getPriviledges()))
				.collect(Collectors.toList());
	}
}