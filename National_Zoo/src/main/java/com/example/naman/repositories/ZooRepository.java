package com.example.naman.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Zoo;

public interface ZooRepository extends JpaRepository<Zoo, Long> {
	
	Page<Zoo> findByArchievedFalse(Pageable pageable);
}
