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

	private String fullName;
	
	private String email;
	
	private String userName;
	
	private String password;
	
	private Long roleId;
	
	private AddressDTO address;
}
