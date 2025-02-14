package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Roles;
import com.example.naman.entities.RolesPriviledges;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */
public interface RolesPriviledgesRepository extends JpaRepository<RolesPriviledges, Long> {
	
	public List<RolesPriviledges> findByRoles(Roles roles); 
		
}
