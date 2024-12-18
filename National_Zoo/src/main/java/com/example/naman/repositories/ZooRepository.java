package com.example.naman.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Animal;
import com.example.naman.entities.Zoo;

public interface ZooRepository extends JpaRepository<Zoo, Long> {
	
	Page<Zoo> findByArchievedFalse(Pageable pageable);
	List<Zoo> findAllByZooIdNotAndArchievedFalse(Long id);

	 @Query("SELECT z FROM Zoo z JOIN z.address a JOIN a.city c JOIN c.state s JOIN s.country co WHERE LOWER(z.zooName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.cityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(s.stateName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(co.countryName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	    List<Zoo> searchByZooNameOrLocation(@Param("searchTerm") String searchTerm);
    
}

