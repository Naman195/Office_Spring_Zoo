package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Roles;
import com.example.naman.entities.RolesPriviledges;

public interface RolesPriviledgesRepository extends JpaRepository<RolesPriviledges, Long> {
	
	public List<RolesPriviledges> findByRoles(Roles roles); 
		
}
