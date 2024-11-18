package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {
	

}
