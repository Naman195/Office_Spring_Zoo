package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
		

}
