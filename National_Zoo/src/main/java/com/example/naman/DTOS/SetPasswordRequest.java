package com.example.naman.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordRequest {

	private String oldPassword;
	
	private String newPassword;
	
	
}
