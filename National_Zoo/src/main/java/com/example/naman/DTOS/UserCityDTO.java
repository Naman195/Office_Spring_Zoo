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
public class UserCityDTO {

	@NotNull(message = "City Id is not  Null")
	private Long cityId;
	
	private String cityName;
	
	@Valid
	private UserStateDTO state;
	
}
