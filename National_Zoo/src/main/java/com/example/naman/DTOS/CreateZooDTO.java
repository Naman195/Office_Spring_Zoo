package com.example.naman.DTOS;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateZooDTO {
	@NotNull(message = "Zoo Name is Required")
	private String zooName;
	@Valid
	private AddressDTO address;

}
