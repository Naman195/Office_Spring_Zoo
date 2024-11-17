package com.example.naman.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ZooCountryDTO {
	
	@NotNull(message = "Country Id is not  Null")
	private Long countryId;
	
	private String countryName;
	
	

}
