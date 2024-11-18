package com.example.naman.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
	
	private Long userId;

	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private UserAddressDTO address;
}