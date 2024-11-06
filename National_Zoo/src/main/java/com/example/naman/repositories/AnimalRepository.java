package com.example.naman.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
	
	Page<Animal> findByZooZooId(Long id, Pageable pageable);
	
	Page<Animal> findByArchievedFalse(Pageable pageable);
	
	
		
}
