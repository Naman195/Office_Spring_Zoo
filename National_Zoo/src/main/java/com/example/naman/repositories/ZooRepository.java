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
	
	
//	List<Zoo> findByArchievedFalseAndZooNameContainingIgnoreCase(String zooName);
//	List<Zoo> findByArchievedFalseAndAddress_City_State_Country_CountryNameContainingIgnoreCase(String countryName);
//	List<Zoo> findByArchievedFalseAndAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCase(String countryName, String stateName);
//
//    List<Zoo> findByArchievedFalseAndAddress_City_State_Country_CountryNameContainingIgnoreCaseAndAddress_City_State_StateNameContainingIgnoreCaseAndAddress_City_CityNameContainingIgnoreCase(
//            String countryName, String stateName, String cityName);
    
    
//    List<Zoo> findByArchievedFalseAndZooNameOrAddress_City_State_Country_CountryNameContainingIgnoreCaseOrAddress_City_State_StateNameContainingIgnoreCaseOrAddress_City_CityNameContainingIgnoreCase(String query);
    
//	@Query(value = 'select z from Zoo z Join Address a on z.address_id = a.address_id Join City c on a.city_id = c.city_id Join State s on c.state_id = s.state_id Join Country co on  s.country_id = co.country_id where LOWER(z.zooName) LIKE :query OR LOWER(c.cityName) LIKE :query OR LOWER(s.stateName) LIKE :query OR LOWER(co.countryName) LIKE :query' , nativeQuery = true)
    @Query(value = 'select * from Zoo z Join Address a on z.address_id = a.address_id Join City c on a.city_id = c.city_id Join State s on c.state_id = s.state_id Join Country co on  s.country_id = co.country_id where lower(z.zoo_name) like :query or lower(c.city_name) like :query or lower(s.state_name) like :query or lower(co.country_name) like :query')
	List<Zoo> findByZooSearch(@Param("query") String query);
    
}



//Sql WorkBench Query

//select * from zoo z 
//join address a on z.address_id = a.address_id
//join city c on a.city_id =  c.city_id
//join state s on c.state_id = s.state_id
//join country co on s.country_id = co.country_id
//where lower(z.zoo_name) like '%up%'
// or lower(c.city_name) like '%up%'
//or lower(s.state_name) like '%up%'
//or lower(co.country_name) like '%up%';
