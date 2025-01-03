package com.example.naman.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassworsDTO {

	private String newPassword;
	
	private String tokenKey;
}
