package com.example.naman.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naman.entities.Roles;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
	
	public Optional<Roles> findByRoleName(String name);
	
}
