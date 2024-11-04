package com.example.naman.DTOS;





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
public class AddressDTO {

	@NotNull(message =  "Street name is not null")
	private String street;
	@NotNull(message =  "Zip Code  is not null")
	private String zipCode;
	@Valid
	private cityDTO city;
}
