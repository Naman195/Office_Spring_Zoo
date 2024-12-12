package com.example.naman.DTOS;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnimalData_TransferHistoryDataDTO {
	
	private AnimalResponseDTO animalData;
	
	private List<TransferHistoryResponseDTO> transferData;

}
