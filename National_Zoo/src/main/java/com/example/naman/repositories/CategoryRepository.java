package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.CategoryTypes;

public interface CategoryRepository extends JpaRepository<CategoryTypes, Long> {

}
