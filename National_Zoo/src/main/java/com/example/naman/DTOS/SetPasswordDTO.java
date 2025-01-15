package com.example.naman.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordDTO {

	private String newPassword;
	
	private String tokenKey;
}
