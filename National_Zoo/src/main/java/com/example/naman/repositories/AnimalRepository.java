package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Animal;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */

public interface AnimalRepository extends JpaRepository<Animal, Long> {
	
	Page<Animal> findByArchievedFalseAndZooZooId(Long id, Pageable pageable);
	List<Animal> findByArchievedFalseAndZooZooId(Long id);
	
	Page<Animal> findByArchievedFalse(Pageable pageable);
	
	List<Animal> findByAnimalNameContainingIgnoreCaseOrAnimalTypeContainingIgnoreCaseAndZoo_ZooId(
            String animalName, String animalType, Long zooId);
	
}
