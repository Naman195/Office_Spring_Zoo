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

	private String fullName;
	
	private String email;
	
	private String username;
	
	private String image;
	
	private UserAddressDTO address;
}
