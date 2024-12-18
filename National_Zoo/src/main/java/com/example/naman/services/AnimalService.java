package com.example.naman.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.naman.DTOS.AnimalData_TransferHistoryDataDTO;
import com.example.naman.DTOS.AnimalResponseDTO;
import com.example.naman.DTOS.CreateAnimalDTO;
import com.example.naman.DTOS.TransferHistoryResponseDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Animal;
import com.example.naman.entities.TransferHistory;
import com.example.naman.entities.User;
import com.example.naman.entities.Zoo;
import com.example.naman.exceptions.ResourceNotFoundException;
import com.example.naman.repositories.AnimalRepository;
import com.example.naman.repositories.TransferHistoryRepository;
import com.example.naman.repositories.ZooRepository;

@Service
public class AnimalService {

	@Autowired
	private AnimalRepository animalRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ZooRepository zooRepository;
	
	@Autowired
	private TransferHistoryRepository transferHistoryRepository;
	
	public AnimalResponseDTO addAnimal(CreateAnimalDTO animal)
	{
		Animal addAnimal = modelMapper.map(animal, Animal.class);
		Animal savedAnimal =  animalRepository.save(addAnimal);
		
		AnimalResponseDTO responseDTO = modelMapper.map(savedAnimal, AnimalResponseDTO.class);
		return responseDTO;
	}
	
	public Page<Animal> getAllAnimals(Pageable pageable)
	{
		return animalRepository.findByArchievedFalse(pageable);	
	}
	
	public Page<AnimalResponseDTO> getAnimalByZooId(Long id, Pageable pageable)
	{
		
		Page<Animal> allanimalsInZoo =  animalRepository.findByArchievedFalseAndZooZooId(id, pageable);
		
		
		
	    List<AnimalResponseDTO> animalResponseDTOs = allanimalsInZoo.getContent().stream()
	            .map(animal -> modelMapper.map(animal, AnimalResponseDTO.class))
	            .collect(Collectors.toList());

	   
	    return new PageImpl<>(animalResponseDTOs, pageable, allanimalsInZoo.getTotalElements());
		
	}
	
	public AnimalResponseDTO getAnimalById(Long id)
	{
		Animal animal =  animalRepository.findById(id).filter(ani -> !ani.isArchieved())
				.orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		return modelMapper.map(animal, AnimalResponseDTO.class);
	}
	
	public void deletedAnimal(Long id)
	{
		
		Animal ani = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		ani.setArchieved(true); 
		animalRepository.save(ani);
	}
	
	public AnimalResponseDTO updateAnimalById(CreateAnimalDTO updateAnimalDTO, Long id)
	{
		Animal existingAnimal = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		modelMapper.map(updateAnimalDTO, existingAnimal);
		Long zooId = updateAnimalDTO.getZoo().getZooId();
		Zoo zoo = zooRepository.findById(zooId).orElseThrow(() -> new ResourceNotFoundException("Zoo not found with ID: " + zooId));
		existingAnimal.setZoo(zoo);
		Animal updatedAnimal = animalRepository.save(existingAnimal);
		return modelMapper.map(updatedAnimal, AnimalResponseDTO.class);
	
	}
	
	 public List<Animal> searchByNameOrType(String searchTerm, Long zooId) {
	        List<Animal> allAni =  animalRepository.findByAnimalNameContainingIgnoreCaseOrAnimalTypeContainingIgnoreCaseAndZoo_ZooId(
	                searchTerm, searchTerm, zooId);
	        List<Animal> allAnimalInZooByZooid = animalRepository.findByArchievedFalseAndZooZooId(zooId); 
	        List<Animal> fnlList = new ArrayList<>();
	        for(Animal ani: allAnimalInZooByZooid) {
	        	if(allAni.contains(ani)) {
	        		fnlList.add(ani);
	        	}
	        }
	        return fnlList;
	        
	    }
	 
	 
	 public List<ZooResponseDTO> getAllZooExceptCurrentZoo(Long id){
		List<ZooResponseDTO> allZoo = zooRepository.findAllByZooIdNotAndArchievedFalse(id)
												.stream()
												.map(zoo -> modelMapper.map(zoo, ZooResponseDTO.class)).collect(Collectors.toList());
		
			return allZoo;
		 
		 
	 }
	 
	 
	 public AnimalResponseDTO transferAnimal(Long animalId, Long newZooId) {
		 
		 Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new ResourceNotFoundException("AnimalId is not Valid"));
		 
		 Zoo newZoo = zooRepository.findById(newZooId).orElseThrow(() -> new ResourceNotFoundException("Zoo  Not Found with ZooId"));
		 
		 Zoo currentZoo = animal.getZoo(); // get CurrentZoo where Animal Belongs.
		 
		 animal.setZoo(newZoo);
		 animalRepository.save(animal);
		 
		 TransferHistory transferHistory = new TransferHistory();
		 
		 transferHistory.setAnimalId(animal);
		 transferHistory.setFromZoo(currentZoo);
		 transferHistory.setToZoo(newZoo);
		 
		 // get Current loggedIn User from Security Context
		 
		 User currentUser  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		 transferHistory.setUserName(currentUser.getUsername());
		 
		 transferHistory.setDate(new Date(System.currentTimeMillis()));
		 
		 transferHistoryRepository.save(transferHistory);
		 
		 
		 return modelMapper.map(animal, AnimalResponseDTO.class);
		 
			 
	 }
	 
	 
	 public ResponseEntity<?> animalTransferHistory(Long animalId) {
		    List<TransferHistory> historyList = transferHistoryRepository.findByAnimalId_AnimalId(animalId);

		    if (historyList.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Transfer Hisory Found for this Animal");
		    }
		    
		    AnimalResponseDTO animal = new AnimalResponseDTO();
		    Animal transferredAnimalData = historyList.get(0).getAnimalId();
		    animal.setAnimalName(transferredAnimalData.getAnimalName());
		    animal.setAnimalId(transferredAnimalData.getAnimalId());	
		    animal.setAnimalType(transferredAnimalData.getAnimalType());
		    animal.setZoo(modelMapper.map(transferredAnimalData.getZoo(), ZooResponseDTO.class));
		    AnimalData_TransferHistoryDataDTO  ResponseDTO = new AnimalData_TransferHistoryDataDTO();
		    ResponseDTO.setAnimalData(animal);
		    // Map entities to DTOs
		    List<TransferHistoryResponseDTO> response = historyList.stream()
		        .map(history -> new TransferHistoryResponseDTO(
		            history.getId(),
		            history.getAnimalId().getAnimalName(),
		            history.getFromZoo().getZooName(),
		            history.getToZoo().getZooName(),
		            history.getUserName(),
		            history.getDate().toString()
		        ))
		        .toList();
		    ResponseDTO.setTransferData(response);
		    return ResponseEntity.ok(ResponseDTO);
		}
}
