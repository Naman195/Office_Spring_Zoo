package com.example.naman.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

/**
 * Animal Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
 * */

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
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	/**
	 * this method is used for Add New Animal in a Zoo.
	 * @param animalJSON, animalImage.
	 * @return "Animal added successfully"
	 * 
	 * @author Naman Arora
	 * */
	
	public void addAnimal(CreateAnimalDTO animal, MultipartFile image)
	{
		try {
			Animal addAnimal = modelMapper.map(animal, Animal.class);
			if(image != null && !image.isEmpty()) {
	        	String imageName = saveImage(image);
	        	addAnimal.setImage(imageName);
	        }
			
		 animalRepository.save(addAnimal);
			
//			AnimalResponseDTO responseDTO = modelMapper.map(savedAnimal, AnimalResponseDTO.class);
//			return responseDTO;
			
		} catch (IOException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage());
	    }  catch (Exception e) {
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add Animal: " + e.getMessage());

	    }
		
	}
	
	
	/**
	 * this method is used for save Image in the Upload Directory.
	 * @param image
	 * @return FileName
	 * 
	 * @author Naman Arora
	 * */
	
private String saveImage(MultipartFile image) throws IOException {
		
		if (image.isEmpty()) {
			throw new IllegalArgumentException("Image file is empty");
		}
		
		
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
		Path filePath = uploadPath.resolve(fileName);
		Files.write(filePath, image.getBytes());

		return fileName.toString();	
	}
	
	/**
	 * this method is used for fetch All Animals via ZooId
	 * @param pageable
	 * @return List of Animals Of Pageable
	 * 
	 * @author Naman Arora
	 * */

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
	
	/**
	 * this method is used for get Animal By AnimalId
	 * @param animalId
	 * @return AnimalresponseDTO Animal;
	 * 
	 * @author Naman Arora
	 * */
	
	public AnimalResponseDTO getAnimalById(Long id)
	{
		Animal animal =  animalRepository.findById(id).filter(ani -> !ani.isArchieved())
				.orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		return modelMapper.map(animal, AnimalResponseDTO.class);
	}
	
	/**
	 * this method is used for Archieved the Animal.
	 * @param animalId
	 * @return void
	 * 
	 * @author Naman Arora
	 * */
	
	public void deletedAnimal(Long id)
	{
		
		Animal ani = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		ani.setArchieved(true); 
		animalRepository.save(ani);
	}
	
	/**
	 * this method is used for Update New Animal in a Zoo.
	 * @param updateAnimalDTO, animalImage, id.
	 * @return AnimalResponseDTO
	 * 
	 * @author Naman Arora
	 * */
	
	public AnimalResponseDTO updateAnimalById(CreateAnimalDTO updateAnimalDTO, MultipartFile image,  Long id) throws IOException
	{
		Animal existingAnimal = animalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Animal  Not Found"));
		String oldImageName = existingAnimal.getImage();
		modelMapper.map(updateAnimalDTO, existingAnimal);
		Long zooId = updateAnimalDTO.getZoo().getZooId();
		Zoo zoo = zooRepository.findById(zooId).orElseThrow(() -> new ResourceNotFoundException("Zoo not found with ID: " + zooId));
		existingAnimal.setZoo(zoo);
		if(image != null && !image.isEmpty()) {
			String imageName = saveImage(image);
			existingAnimal.setImage(imageName);        	
		}else {
			existingAnimal.setImage(oldImageName);
		}
		Animal updatedAnimal = animalRepository.save(existingAnimal);
		return modelMapper.map(updatedAnimal, AnimalResponseDTO.class);
	
	}
	
	/**
	 * this method is used for Search  Animal in a Zoo.
	 * @param searchTerm, zooId.
	 * @return Animal List.
	 * 
	 * @author Naman Arora
	 * */
	
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
	 
	 /**
		 * this controller is used for fetchAllZooExceptCurrent.
		 * @param zooid
		 * @return ZooList
		 * 
		 * @author Naman Arora
		 * */
	 
	 public List<ZooResponseDTO> getAllZooExceptCurrentZoo(Long id){
		List<ZooResponseDTO> allZoo = zooRepository.findAllByZooIdNotAndArchievedFalse(id)
												.stream()
												.map(zoo -> modelMapper.map(zoo, ZooResponseDTO.class)).collect(Collectors.toList());
		
			return allZoo;
		 
		 
	 }
	 
	 /**
		 * this controller is used for transfer Animal from currentZoo to new Zoo.
		 * @param animalId, newZooId.
		 * @return AnimalResponseDTO
		 * 
		 * @author Naman Arora
		 * */
	 
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
	 
	 /**
		 * this method is used for get Animal Transfer History.
		 * @param animalId.
		 * @return AnimalTransferHistory.
		 * 
		 * @author Naman Arora
		 * */
	 
	 
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
