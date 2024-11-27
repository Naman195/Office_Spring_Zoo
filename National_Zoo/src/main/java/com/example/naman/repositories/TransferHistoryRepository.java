package com.example.naman.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naman.entities.TransferHistory;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
	

}
