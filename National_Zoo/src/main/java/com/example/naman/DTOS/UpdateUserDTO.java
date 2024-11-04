package com.example.naman.DTOS;

import com.example.naman.entities.Address;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
	
	@NotNull(message =  "First name is not null")
	private String firstName;
	@NotNull(message =  "Last name is not null")
	private String lastName;
	@Valid
	private AddressDTO address;
}
