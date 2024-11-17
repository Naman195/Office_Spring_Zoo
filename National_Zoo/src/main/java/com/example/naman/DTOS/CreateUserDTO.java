package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

	private String firstName;
	
	private String lastName;
	
	private String userName;
	
	private String password;
	
	private String role;
	
	private AddressDTO address;
}
