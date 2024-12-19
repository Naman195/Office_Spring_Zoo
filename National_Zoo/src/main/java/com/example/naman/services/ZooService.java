package com.example.naman.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.naman.DTOS.AddressDTO;
import com.example.naman.DTOS.CreateZooDTO;
import com.example.naman.DTOS.ZooResponseDTO;
import com.example.naman.entities.Address;
import com.example.naman.entities.City;
import com.example.naman.entities.Zoo;
import com.example.naman.exceptions.ResourceNotFoundException;
import com.example.naman.repositories.CityRepository;
import com.example.naman.repositories.ZooRepository;


@Service
public class ZooService {
	
	@Autowired
	private  ZooRepository zooRepository; 
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	

	public void createZoo(CreateZooDTO zoo, MultipartFile image)
	{
		try {
	        Zoo newZoo = modelMapper.map(zoo, Zoo.class);
	        if(image != null && !image.isEmpty()) {
	        	String imageName = saveImage(image);
	        	newZoo.setImage(imageName);
	        }
	        zooRepository.save(newZoo);
	    
		} catch (IOException e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage());
	    }  catch (Exception e) {
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create zoo: " + e.getMessage());

	    }
	}
	
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
	
	
	public Page<ZooResponseDTO> getAllZoo(Pageable pageable)
	{  
	    Page<Zoo> allZoo = zooRepository.findByArchievedFalse(pageable);
	    
	    List<ZooResponseDTO> zooResponseDTOs = allZoo.getContent().stream()
	            .map(zoo -> modelMapper.map(zoo, ZooResponseDTO.class))
	            .collect(Collectors.toList());
	    
	    return new PageImpl<>(zooResponseDTOs, pageable, allZoo.getTotalElements());
	}

	
	public ZooResponseDTO getZooById(Long id)
	{
		Zoo zoo =  zooRepository.findById(id)
				.filter(z -> !z.isArchieved())
				.orElseThrow(() ->  new ResourceNotFoundException("Zoo not found with ID: " + id));
		return modelMapper.map(zoo, ZooResponseDTO.class);
	}
	
	public void deleteZooById(Long id) {
		Zoo zoo = zooRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zoo Not Found By Id"));
		zoo.setArchieved(true);
		zooRepository.save(zoo);
	}
	
	public ZooResponseDTO updateZooById(CreateZooDTO updateZooDTO, MultipartFile image,  Long id) throws IOException
	{
		Zoo existingZoo = zooRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zoo Not Found By Id"));
		existingZoo.setZooName(updateZooDTO.getZooName());
		Address address = existingZoo.getAddress();
		AddressDTO addressDTO = updateZooDTO.getAddress();
		if(addressDTO !=  null) {
			address.setStreet(addressDTO.getStreet());
			address.setZipCode(addressDTO.getZipCode());
			
			City city = cityRepository.findById(addressDTO.getCity().getCityId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
			address.setCity(city);			
		}
		
		if(image != null && !image.isEmpty()) {
			String imageName = saveImage(image);
			existingZoo.setImage(imageName);        	
		}
		
		Zoo  updatedZoo = zooRepository.save(existingZoo);
		return modelMapper.map(updatedZoo, ZooResponseDTO.class);
	}
	
	
	public List<ZooResponseDTO> searchByNameOrLocation(String searchItem){
		
		List<Zoo> searchList =  zooRepository.searchByZooNameOrLocation(searchItem);
		List<Zoo> filteredZooList = searchList.stream().filter(zoo -> !zoo.isArchieved()).collect(Collectors.toList());
	 List<ZooResponseDTO> searchDtoList = filteredZooList.stream().map(search -> modelMapper.map(search, ZooResponseDTO.class)).collect(Collectors.toList());
	 return searchDtoList;
	 

    }
		
}
