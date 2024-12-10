package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistoryResponseDTO {
	
	private Long id;
    private String animalName;
    private String fromZooName;
    private String toZooName;
    private String userName;
    private String date;
}


