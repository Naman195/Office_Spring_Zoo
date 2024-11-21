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
	
	@NotNull(message =  "Name is not null")
	private String fullName;
	@NotNull(message =  "Email is not null")
	private String email;
	@Valid
	private AddressDTO address;
}
