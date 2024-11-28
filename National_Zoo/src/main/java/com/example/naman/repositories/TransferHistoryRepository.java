package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.Animal;
import com.example.naman.entities.TransferHistory;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
	
	public List<TransferHistory> findByAnimalId_AnimalId(Long id);

}
