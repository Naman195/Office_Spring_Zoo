package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.TransferHistory;

/**
 * Documentation
 *
 * @author Naman Arora
 * @version 1.0.0
 * @since 14-Feb-2025
 */
public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
	
	public List<TransferHistory> findByAnimalId_AnimalId(Long id);

}
