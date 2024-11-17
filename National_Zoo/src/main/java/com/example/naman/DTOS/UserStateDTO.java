package com.example.naman.DTOS;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStateDTO {

	@NotNull(message = "State Id is not  Null")
	private Long stateId;
	
	private String stateName;
	
	@Valid
	private UserCountryDTO country;
}
