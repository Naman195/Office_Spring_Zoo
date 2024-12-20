package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AnimalResponseDTO {

	
	private Long animalId;
	
	private String animalName;
	
	private String animalType;
	
	private String image;
	
	private ZooResponseDTO zoo;
	
	
}
